package ss.agrolavka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.base.lang.ThrowingSupplier;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CartUtils {

    public static Boolean inCart(Product product, List<OrderPosition> orderPositions) {
        return orderPositions.stream()
            .filter(pos -> Objects.equals(product.getId(), pos.getProductId())).findFirst().isPresent();
    }

    public static String inCartVariants(Product product, List<OrderPosition> orderPositions) {
        final var inCartVariants = orderPositions.stream()
                .filter(pos -> pos.getVariantId() != null && pos.getProductId().equals(product.getId()))
                .map(OrderPosition::getVariantId).collect(Collectors.toSet());
        return ((ThrowingSupplier<String>) () ->
                new ObjectMapper().writeValueAsString(inCartVariants).replace("\"", "'")
        ).get();
    }
}
