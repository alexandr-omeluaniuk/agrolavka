/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.service;

import java.util.List;
import java.util.Map;
import ss.entity.agrolavka.Discount;
import ss.entity.agrolavka.PriceType;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.images.storage.EntityImage;

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
     * Attach images to product.
     * @param product product with images.
     * @throws Exception error.
     */
    void attachImagesToProduct(Product product) throws Exception;
    /**
     * Remove product images.
     * @param product product.
     * @throws Exception error.
     */
    void removeProductImages(Product product) throws Exception;
    /**
     * Get price types.
     * @return price type.
     * @throws Exception error.
     */
    List<PriceType> getPriceTypes() throws Exception;
    /**
     * Create products group.
     * @param group products group.
     * @return external ID.
     * @throws Exception error.
     */
    String createProductsGroup(ProductsGroup group) throws Exception;
    /**
     * Delete product group.
     * @param group group.
     * @throws Exception error.
     */
    void deleteProductsGroup(ProductsGroup group) throws Exception;
    /**
     * Update products group.
     * @param group products group.
     * @throws Exception error.
     */
    void updateProductsGroup(ProductsGroup group) throws Exception;
    /**
     * Get stock.
     * @param limit limit.
     * @param offset offset.
     * @return map, where key is product code, value is product with quantity.
     * @throws Exception error.
     */
    Map<String, Product> getStock(int offset, int limit) throws Exception;
    /**
     * Get discounts.
     * @return discounts.
     * @throws Exception error.
     */
    List<Discount> getDiscounts() throws Exception;
}
