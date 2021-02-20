/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import java.util.List;
import ss.agrolavka.model.Product;
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
     */
    List<Product> search(ProductsSearchRequest request);
    /**
     * Count of products.
     * @param request search request.
     * @return count of products.
     */
    Long count(ProductsSearchRequest request);
}
