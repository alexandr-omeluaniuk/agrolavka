package ss.agrolavka.service;

import ss.agrolavka.wrapper.ProductsSearchResponse;

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
}
