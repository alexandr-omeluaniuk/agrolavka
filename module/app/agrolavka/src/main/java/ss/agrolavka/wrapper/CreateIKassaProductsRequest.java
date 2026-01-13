package ss.agrolavka.wrapper;

import java.util.List;

public record CreateIKassaProductsRequest(
    List<iKassaProduct> sku
) {
}
