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
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.Product;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.util.PlatformEntityListener;

/**
 * Product entity listener.
 * @author alex
 */
@Component
@Qualifier("ProductEntityListener")
class ProductEntityListener extends EntityWithImagesListener implements PlatformEntityListener<Product> {
    /** MySklad integration service. */
    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;

    @Override
    public Class entity() {
        return Product.class;
    }

    @Override
    public void prePersist(Product entity) throws Exception {
        Product mySkladEntity = mySkladIntegrationService.createProduct(entity);
        cropImages(entity.getImages());
        entity.setExternalId(mySkladEntity.getExternalId());
        mySkladIntegrationService.attachImagesToProduct(entity);
    }
    
    @Override
    public void preUpdate(Product entity) throws Exception {
        mySkladIntegrationService.updateProduct(entity);
        Product entityFromDB = coreDAO.findById(entity.getId(), Product.class);
        final List<EntityImage> actualImages = getActualImages(entityFromDB.getImages(), entity.getImages());
        entityFromDB.setImages(actualImages);
        entityFromDB = coreDAO.update(entityFromDB);
        entity.setImages(entityFromDB.getImages());
        mySkladIntegrationService.removeProductImages(entity);
        mySkladIntegrationService.attachImagesToProduct(entity);
    }
    
    @Override
    public void preDelete(Set<Long> ids) throws Exception {
        for (Long id : ids) {
            Product product = coreDAO.findById(id, Product.class);
            mySkladIntegrationService.deleteProduct(product);
        }
    }
    
}
