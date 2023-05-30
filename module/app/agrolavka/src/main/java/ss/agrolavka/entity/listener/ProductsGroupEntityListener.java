/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.entity.listener;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.images.storage.EntityImage;
import ss.martin.core.dao.CoreDao;
import ss.martin.platform.util.PlatformEntityListener;

/**
 * Products group entity listener.
 * @author alex
 */
@Component
@Qualifier("ProductsGroupEntityListener")
class ProductsGroupEntityListener extends EntityWithImagesListener implements PlatformEntityListener<ProductsGroup> {
    /** MySklad integration service. */
    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    
    @Override
    public Class<ProductsGroup> entity() {
        return ProductsGroup.class;
    }

    @Override
    public void prePersist(ProductsGroup entity) {
        //cropImages(entity.getImages(), SiteConstants.IMAGE_THUMB_SIZE);
        entity.setUrl(UrlProducer.transliterate(entity.getName()));
        entity.setExternalId(mySkladIntegrationService.createProductsGroup(entity));
    }
    
    @Override
    public void postPersist(ProductsGroup entity) {
        AppCache.flushCache(coreDAO.getAll(ProductsGroup.class));
    }

    @Override
    public void preUpdate(ProductsGroup entity) {
        ProductsGroup entityFromDB = coreDAO.findById(entity.getId(), ProductsGroup.class);
        final List<EntityImage> actualImages = getActualImages(
                entityFromDB.getImages(), entity.getImages(), SiteConstants.IMAGE_THUMB_SIZE);
        entityFromDB.setImages(actualImages);
        entity.setImages(actualImages);
        coreDAO.update(entityFromDB);
        entity.setUrl(UrlProducer.transliterate(entity.getName()));
        mySkladIntegrationService.updateProductsGroup(entity);
    }
    
    @Override
    public void postUpdate(ProductsGroup entity) {
        AppCache.flushCache(coreDAO.getAll(ProductsGroup.class));
    }

    @Override
    public void preDelete(Set<Long> ids) {
        for (Long id : ids) {
            ProductsGroup group = coreDAO.findById(id, ProductsGroup.class);
            mySkladIntegrationService.deleteProductsGroup(group);
        }
    }
}
