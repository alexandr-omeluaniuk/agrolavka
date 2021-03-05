/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.util;

import javax.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ss.agrolavka.entity.Product;
import ss.agrolavka.service.MySkladIntegrationService;

/**
 * Product entity listener.
 * @author alex
 */
public class ProductEntityListener {
    /** MySklad integration service. */
    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;
    @PrePersist
    public void prePersist(Product product) throws Exception {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        if (product.getExternalId() == null) {
            product = mySkladIntegrationService.createProduct(product);
        }
    }
}
