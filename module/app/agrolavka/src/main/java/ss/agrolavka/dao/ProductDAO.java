/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Discount;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
    void resetDiscounts(List<Discount> discounts);
    /**
     * Get last modified products.
     * @param minDate min date of modification (includes the date).
     * @return list of products.
     */
    List<Product> getLastModifiedProducts(Date minDate);

    List<Product> getByExternalIds(List<String> externalIds);

    List<Product> getByIds(Set<Long> ids);

    /**
     * Get products what were ordered together with target product.
     * @param productId - target product ID.
     */
    List<Product> getRelatedProducts(Long productId);
}
