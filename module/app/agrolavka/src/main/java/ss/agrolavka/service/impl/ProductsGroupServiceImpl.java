package ss.agrolavka.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.service.ProductsGroupService;
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
    
    private void resetCache() {
        Optional.ofNullable(cacheManager.getCache(CacheKey.PRODUCTS_GROUPS)).ifPresent(cache -> cache.clear());
    }
}
