package ss.agrolavka.service;

import java.util.List;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductVariant;

/**
 * Product service.
 * @author alex
 */
public interface ProductService {
    
    /**
     * Quick search of products.
     * @param searchText search test.
     * @return search result.
     */
    ProductsSearchResponse quickSearchProducts(String searchText);
    
    /**
     * Get new products.
     * @return new products.
     */
    List<Product> getNewProducts();
    
    /**
     * Get all products with discount.
     * @return products with discount.
     */
    List<Product> getProductsWithDiscount();
    
    /**
     * Get all products count.
     * @return products count.
     */
    Long getProductsCount();
    
    /**
     * Search products.
     * @param request search request.
     * @return list of products.
     */
    List<Product> search(ProductsSearchRequest request);
    
    /**
     * Get product variants.
     * @param externalId product external ID.
     * @return list of product variants.
     */
    List<ProductVariant> getVariants(String externalId);
}
