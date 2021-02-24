/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import java.util.List;
import java.util.Set;
import ss.agrolavka.model.Product;
import ss.agrolavka.model.ProductsGroup;
import ss.agrolavka.wrapper.ProductsSearchRequest;

/**
 * Product DAO.
 * @author alex
 */
public interface ProductDAO {
    /**
     * Search products.
     * @param request search request.
     * @return list of products.
     * @throws Exception error.
     */
    List<Product> search(ProductsSearchRequest request) throws Exception ;
    /**
     * Count of products.
     * @param request search request.
     * @return count of products.
     * @throws Exception error.
     */
    Long count(ProductsSearchRequest request) throws Exception;
    /**
     * Get catalog product groups.
     * @return catalog product groups.
     */
    Set<ProductsGroup> getCatalogProductGroups();
}
