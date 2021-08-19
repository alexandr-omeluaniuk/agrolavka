/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.entity.listener;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.service.ImageService;
import ss.martin.platform.util.PlatformEntityListener;

/**
 * Products group entity listener.
 * @author alex
 */
@Component
@Qualifier("ProductsGroupEntityListener")
class ProductsGroupEntityListener implements PlatformEntityListener<ProductsGroup> {
    /** MySklad integration service. */
    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Image service. */
    @Autowired
    private ImageService imageService;
    
    @Override
    public Class<ProductsGroup> entity() {
        return ProductsGroup.class;
    }

    @Override
    public void prePersist(ProductsGroup entity) throws Exception {
        for (EntityImage image : entity.getImages()) {
            image.setImageData(imageService.convertToThumbnail(image.getImageData(), SiteConstants.IMAGE_THUMB_SIZE));
        }
        entity.setHasImages(!entity.getImages().isEmpty());
        entity.setUrl(UrlProducer.transliterate(entity.getName()));
        entity.setExternalId(mySkladIntegrationService.createProductsGroup(entity));
    }
    
    @Override
    public void postPersist(ProductsGroup entity) throws Exception {
        AppCache.flushCache(coreDAO.getAll(ProductsGroup.class));
    }

    @Override
    public void preUpdate(ProductsGroup entity) throws Exception {
        ProductsGroup entityFromDB = coreDAO.findById(entity.getId(), ProductsGroup.class);
        for (EntityImage image : entity.getImages()) {
            image.setImageData(imageService.convertToThumbnail(image.getImageData(), SiteConstants.IMAGE_THUMB_SIZE));
        }
        entityFromDB.setImages(entity.getImages());
        entityFromDB.setHasImages(!entity.getImages().isEmpty());
        coreDAO.update(entityFromDB);
        entity.setUrl(UrlProducer.transliterate(entity.getName()));
        mySkladIntegrationService.updateProductsGroup(entity);
    }
    
    @Override
    public void postUpdate(ProductsGroup entity) throws Exception {
        AppCache.flushCache(coreDAO.getAll(ProductsGroup.class));
    }

    @Override
    public void preDelete(Set<Long> ids) throws Exception {
        for (Long id : ids) {
            ProductsGroup group = coreDAO.findById(id, ProductsGroup.class);
            mySkladIntegrationService.deleteProductsGroup(group);
        }
    }
}
