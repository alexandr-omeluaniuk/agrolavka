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
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.platform.dao.CoreDAO;
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
    
    @Override
    public Class<ProductsGroup> entity() {
        return ProductsGroup.class;
    }

    @Override
    public void prePersist(ProductsGroup entity) throws Exception {
        entity.setExternalId(mySkladIntegrationService.createProductsGroup(entity));
    }

    @Override
    public void preUpdate(ProductsGroup entity) throws Exception {
        mySkladIntegrationService.updateProductsGroup(entity);
    }

    @Override
    public void preDelete(Set<Long> ids) throws Exception {
        for (Long id : ids) {
            ProductsGroup group = coreDAO.findById(id, ProductsGroup.class);
            mySkladIntegrationService.deleteProductsGroup(group);
        }
    }
}
