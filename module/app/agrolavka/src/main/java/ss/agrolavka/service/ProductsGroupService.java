package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.core.dao.CoreDao;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Products group service implementation.
 * @author alex
 */
@Service
public class ProductsGroupService {
    
    @Autowired
    private MySkladIntegrationService mySklad;

    @Autowired
    private AllProductGroupsService allProductGroupsService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private CoreDao coreDao;

    public ProductsGroup create(final ProductsGroup group) {
        group.setUrl(UrlProducer.transliterate(group.getName()));
        group.setExternalId(mySklad.createProductsGroup(group));
        final var newEntity = coreDao.create(group);
        resetCache();
        return newEntity;
    }

    public ProductsGroup update(final ProductsGroup group) {
        group.setUrl(UrlProducer.transliterate(group.getName()));
        mySklad.updateProductsGroup(group);
        final var updatedEntity = coreDao.update(group);
        resetCache();
        return updatedEntity;
    }

    public void delete(final Long id) {
        final var group = coreDao.findById(id, ProductsGroup.class);
        Optional.ofNullable(group).ifPresent(entity -> {
            mySklad.deleteProductsGroup(group);
            coreDao.delete(id, ProductsGroup.class);
            resetCache();
        });
    }

    public List<ProductsGroup> getTopCategories() {
        return allProductGroupsService.getAllGroups().stream()
            .filter(group -> group.isTopCategory() != null && group.isTopCategory())
            .sorted()
            .collect(
                Collectors.toList()
            );
    }
    
    public List<ProductsGroup> getRootProductGroups() {
        final var rootGroups = allProductGroupsService.getAllGroups().stream().filter(group -> {
            return group.getParentId() == null;
        }).collect(Collectors.toList());
        Collections.sort(rootGroups);
        return rootGroups;
    }
    
    public List<ProductsGroup> getBreadcrumbPath(final ProductsGroup group) {
        final var path = new ArrayList<ProductsGroup>();
        var current = group;
        final var groups = allProductGroupsService.getAllGroups();
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
    
    public Map<String, List<ProductsGroup>> getCategoriesTree() {
        final var tree = new HashMap<String, List<ProductsGroup>>();
        for (final var group : allProductGroupsService.getAllGroups()) {
            final var parentId = Optional.ofNullable(group.getParentId()).orElse("-1");
            if (!tree.containsKey(parentId)) {
                tree.put(parentId, new ArrayList<>());
            }
            tree.get(parentId).add(group);
        }
        return tree;
    }
    
    private void resetCache() {
        Optional.ofNullable(cacheManager.getCache(CacheKey.PRODUCTS_GROUPS)).ifPresent(cache -> cache.clear());
    }
}
