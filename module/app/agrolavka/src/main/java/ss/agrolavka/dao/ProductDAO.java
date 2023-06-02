/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import java.util.Date;
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
     */
    List<Product> search(ProductsSearchRequest request);
    /**
     * Count of products.
     * @param request search request.
     * @return count of products.
     */
    Long count(ProductsSearchRequest request);
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
    /**
     * Get last modified products.
     * @param minDate min date of modification (includes the date).
     * @return list of products.
     */
    List<Product> getLastModifiedProducts(Date minDate);
}
