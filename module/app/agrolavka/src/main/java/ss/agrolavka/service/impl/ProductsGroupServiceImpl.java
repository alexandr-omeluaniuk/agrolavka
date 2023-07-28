package ss.agrolavka.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.service.ProductsGroupService;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.core.dao.CoreDao;

/**
 * Products group service implementation.
 * @author alex
 */
@Service
class ProductsGroupServiceImpl implements ProductsGroupService {
    
    @Autowired
    private MySkladIntegrationService mySklad;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private CoreDao coreDao;

    @Override
    public ProductsGroup create(final ProductsGroup group) {
        group.setUrl(UrlProducer.transliterate(group.getName()));
        group.setExternalId(mySklad.createProductsGroup(group));
        final var newEntity = coreDao.create(group);
        resetCache();
        return newEntity;
    }

    @Override
    public ProductsGroup update(final ProductsGroup group) {
        group.setUrl(UrlProducer.transliterate(group.getName()));
        mySklad.updateProductsGroup(group);
        final var updatedEntity = coreDao.update(group);
        resetCache();
        return updatedEntity;
    }

    @Override
    public void delete(final Long id) {
        final var group = coreDao.findById(id, ProductsGroup.class);
        Optional.ofNullable(group).ifPresent(entity -> {
            mySklad.deleteProductsGroup(group);
            coreDao.delete(id, ProductsGroup.class);
            resetCache();
        });
    }

    @Override
    public List<ProductsGroup> getTopCategories() {
        final var topCategories = getAllGroups().stream().filter(group -> {
            return group.isTopCategory() != null && group.isTopCategory();
        }).collect(Collectors.toList());
        Collections.sort(topCategories);
        return topCategories;
    }
    
    @Override
    @Cacheable(CacheKey.PRODUCTS_GROUPS)
    public List<ProductsGroup> getAllGroups() {
        return coreDao.getAll(ProductsGroup.class);
    }
    
    @Override
    public List<ProductsGroup> getRootProductGroups() {
        final var rootGroups = getAllGroups().stream().filter(group -> {
            return group.getParentId() == null;
        }).collect(Collectors.toList());
        Collections.sort(rootGroups);
        return rootGroups;
    }
    
    public List<ProductsGroup> getBreadcrumbPath(final ProductsGroup group) {
        final var path = new ArrayList<ProductsGroup>();
        var current = group;
        final var groups = getAllGroups();
        while (current != null) {
            path.add(current);
            final var parentId = current.getParentId();
            if (parentId != null) {
                current = groups.stream().filter(g -> {
                    return parentId.equals(g.getExternalId());
                }).findFirst().get();
            } else {
                current = null;
            }
        }
        Collections.reverse(path);
        return path;
    }
    
    private void resetCache() {
        Optional.ofNullable(cacheManager.getCache(CacheKey.PRODUCTS_GROUPS)).ifPresent(cache -> cache.clear());
    }
}
