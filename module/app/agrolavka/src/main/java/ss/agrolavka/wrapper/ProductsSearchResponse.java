package ss.agrolavka.wrapper;

import java.util.List;
import ss.entity.agrolavka.Product;

/**
 * Products search response.
 * @author alex
 */
public record ProductsSearchResponse(
    List<Product> data,
    Long count
) {}
