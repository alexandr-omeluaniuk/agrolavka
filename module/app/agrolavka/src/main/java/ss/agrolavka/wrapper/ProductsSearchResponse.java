package ss.agrolavka.wrapper;

import ss.entity.agrolavka.Product;

import java.util.List;

/**
 * Products search response.
 * @author alex
 */
public record ProductsSearchResponse(
    List<Product> data,
    Long count,

    String searchText
) {}
