/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.entity.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ss.agrolavka.entity.Product;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.util.PlatformEntityListener;

/**
 * Product entity listener.
 * @author alex
 */
@Component
@Qualifier("ProductEntityListener")
public class ProductEntityListener implements PlatformEntityListener<Product> {
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
        entity.setExternalId(mySkladEntity.getExternalId());
    }
    
    @Override
    public void preUpdate(Product entity) throws Exception {
        mySkladIntegrationService.updateProduct(entity);
        Product entityFromDB = coreDAO.findById(entity.getId(), Product.class);
        entityFromDB.setImages(entity.getImages());
        coreDAO.update(entityFromDB);
    }
    
}
