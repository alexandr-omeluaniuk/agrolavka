/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.service;

import java.util.List;
import ss.agrolavka.model.ProductsGroup;

/**
 * My Sklad integration service.
 * @author alex
 */
public interface MySkladIntegrationService {
    /**
     * Authentication.
     * @throws Exception error.
     */
    void authentication() throws Exception;
    /**
     * Get product groups.
     * @return list of product groups.
     * @throws Exception error.
     */
    List<ProductsGroup> getProductGroups() throws Exception;
}
