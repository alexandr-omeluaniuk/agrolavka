package ss.agrolavka.service;

import java.util.List;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.entity.agrolavka.Product;

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
}
