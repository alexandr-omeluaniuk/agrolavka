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
     */
    List<ProductsGroup> getProductGroups();
    
    /**
     * Get products.
     * @param offset offset.
     * @param limit limit.
     * @return products.
     */
    List<Product> getProducts(int offset, int limit);
    
    /**
     * Get product images.
     * @param productExternalId product external ID.
     * @return list of images.
     */
    List<EntityImage> getProductImages(String productExternalId);
    
    /**
     * Create product.
     * @param product product.
     * @return product with external ID.
     */
    Product createProduct(Product product);
    
    /**
     * Update product.
     * @param product product.
     * @return product.
     */
    Product updateProduct(Product product);
    
    /**
     * Delete product.
     * @param product product.
     */
    void deleteProduct(Product product);
    
    /**
     * Attach images to product.
     * @param product product with images.
     */
    void attachImagesToProduct(Product product);
    
    /**
     * Remove product images.
     * @param product product.
     */
    void removeProductImages(Product product);
    
    /**
     * Get price types.
     * @return price type.
     */
    List<PriceType> getPriceTypes();
    
    /**
     * Create products group.
     * @param group products group.
     * @return external ID.
     */
    String createProductsGroup(ProductsGroup group);
    
    /**
     * Delete product group.
     * @param group group.
     */
    void deleteProductsGroup(ProductsGroup group);
    
    /**
     * Update products group.
     * @param group products group.
     */
    void updateProductsGroup(ProductsGroup group);
    
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
