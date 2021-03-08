/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.service;

import java.util.List;
import ss.agrolavka.entity.PriceType;
import ss.agrolavka.entity.Product;
import ss.agrolavka.entity.ProductsGroup;
import ss.martin.platform.entity.EntityImage;

/**
 * My Sklad integration service.
 * @author alex
 */
public interface MySkladIntegrationService {
    /**
     * Get product groups.
     * @return list of product groups.
     * @throws Exception error.
     */
    List<ProductsGroup> getProductGroups() throws Exception;
    /**
     * Get products.
     * @param offset offset.
     * @param limit limit.
     * @return products.
     * @throws Exception error.
     */
    List<Product> getProducts(int offset, int limit) throws Exception;
    /**
     * Get product images.
     * @param productExternalId product external ID.
     * @return list of images.
     * @throws Exception error.
     */
    List<EntityImage> getProductImages(String productExternalId) throws Exception;
    /**
     * Create product.
     * @param product product.
     * @return product with external ID.
     * @throws Exception error.
     */
    Product createProduct(Product product) throws Exception;
    /**
     * Update product.
     * @param product product.
     * @return product.
     * @throws Exception error.
     */
    Product updateProduct(Product product) throws Exception;
    /**
     * Delete product.
     * @param product product.
     * @throws Exception error.
     */
    void deleteProduct(Product product) throws Exception;
    /**
     * Get price types.
     * @return price type.
     * @throws Exception error.
     */
    List<PriceType> getPriceTypes() throws Exception;
}
