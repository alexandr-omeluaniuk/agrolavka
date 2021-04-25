/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import java.util.List;
import java.util.Set;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;

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
    /**
     * Delete products from groups that are not belongs to group external IDs.
     * @param groupExternalIds group external IDs.
     */
    void deleteProductsByNotProductGroupIDs(Set<String> groupExternalIds);
    /**
     * Get product by URL.
     * @param url url.
     * @return product.
     */
    Product getProductByUrl(String url);
    /**
     * Reset discounts.
     */
    void resetDiscounts();
}
