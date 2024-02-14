package ss.agrolavka.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.core.dao.CoreDao;

@Service
public class OrderPositionService {
    
    @Autowired
    private CoreDao coreDao;
    
    @Autowired
    private ProductService productService;
    
    public List<OrderPosition> getPositions(Order order) {
        final var productIds = order.getPositions().stream().map(OrderPosition::getProductId).collect(Collectors.toSet());
        final var productsMap = coreDao.findByIds(productIds, Product.class).stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));
        order.getPositions().forEach(pos -> {
            pos.setProduct(productsMap.get(pos.getProductId()));
            if (pos.getVariantId() != null) {
                final var productVariants = productService.getVariants(pos.getProduct().getExternalId());
                pos.setVariant(
                    productVariants.stream()
                        .filter(v -> v.getExternalId().equals(pos.getVariantId()))
                        .findFirst()
                        .orElse(null)
                );
            }
        });
        return order.getPositions();
    }
}
