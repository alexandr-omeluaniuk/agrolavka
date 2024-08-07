package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.dao.OrderDAO;
import ss.agrolavka.dao.ProductDAO;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.core.dao.CoreDao;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PurchaseHistoryService {

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Cacheable(value = CacheKey.PURCHASE_HISTORY)
    public List<Order> getPurchaseHistory(final String phoneNumber) {
        final var purchaseHistory = orderDAO.getPurchaseHistoryByPhone(phoneNumber);
        if (!purchaseHistory.isEmpty()) {
            final var productIds = purchaseHistory.stream()
                .flatMap(mapper -> mapper.getPositions().stream().map(OrderPosition::getProductId))
                .collect(Collectors.toSet());
            final var productsMap = productDAO.getByIds(productIds).stream().collect(
                Collectors.toMap(Product::getId, Function.identity())
            );
            productsMap.values().forEach(product -> product.setVariants(productService.getVariants(product)));
            purchaseHistory.forEach(order -> {
                order.getPositions().forEach(position -> position.setProduct(productsMap.get(position.getProductId())));
            });
        }
        return purchaseHistory.stream()
            .filter(order -> order.getPositions().stream().anyMatch(position -> position.getProduct() != null))
            .collect(Collectors.toList());
    }
}
