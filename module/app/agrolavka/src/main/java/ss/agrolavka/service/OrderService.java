package ss.agrolavka.service;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.dao.OrderDAO;
import ss.agrolavka.service.impl.TelegramBotOrderService;
import ss.agrolavka.util.PriceCalculator;
import ss.agrolavka.wrapper.CartProduct;
import ss.agrolavka.wrapper.OneClickOrderWrapper;
import ss.agrolavka.wrapper.OrderDetailsWrapper;
import ss.entity.agrolavka.*;
import ss.martin.core.dao.CoreDao;

import java.util.*;

/**
 * Order service implementation.
 * @author alex
 */
@Service
public class OrderService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    /** Core DAO. */
    @Autowired
    private CoreDao coreDao;
    
    @Autowired
    private TelegramBotOrderService telegramBotOrderService;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private PurchaseHistoryService purchaseHistoryService;

    @Autowired
    private OrderDAO orderDao;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Order createOrder(final Order order, final OrderDetailsWrapper orderDetails) throws Exception {
        LOG.info("----------------------------------- Create new order ----------------------------------------------");
        LOG.info("Order total [" + order.calculateTotal() + "]");
        LOG.info("Order phone [" + orderDetails.getPhone() + "]");
        order.setPhone(orderDetails.getPhone());
        order.setComment(orderDetails.getComment());
        order.setCreated(new Date());
        order.setStatus(OrderStatus.WAITING_FOR_APPROVAL);
        if (orderDetails.getCity() != null && !orderDetails.getCity().isBlank()) {
            order.setAddress(getOrderAddress(orderDetails));
            LOG.info("Order delivery type - post");
        } else if (orderDetails.getEuropostLocationId() != null) {
            order.setEuropostLocationSnapshot(getOrderEuropostLocation(orderDetails));
            LOG.info("Order delivery type - europost");
        } else {
            LOG.info("Order delivery type - self");
        }
        // if order was restored from session
        order.setId(null);
        order.getPositions().forEach(position -> position.setId(null));
        final Order savedOrder = coreDao.create(order);
        sendTelegramNotification(savedOrder);
        LOG.info("---------------------------------------------------------------------------------------------------");
        return savedOrder;
    }
    
    public void updateOrder(final Order orderForm) {
        final var order = coreDao.findById(orderForm.getId(), Order.class);
        final var originStatus = order.getStatus();
        order.setAdminComment(orderForm.getAdminComment());
        order.setStatus(orderForm.getStatus());
        coreDao.update(order);
        if (!originStatus.equals(order.getStatus())) {
            telegramBotOrderService.updateExistingOrderMessage(order);
        }
    }
    
    public Order createOneClickOrder(final OneClickOrderWrapper orderDetails) throws Exception {
        final Product product = coreDao.findById(orderDetails.getProductId(), Product.class);
        final var variant = getVariant(orderDetails.getVariantId(), product);
        final List<OrderPosition> positions = new ArrayList<>();
        PriceCalculator.breakQuantityByVolume(product, variant, orderDetails.getQuantity()).forEach((price, quantity) -> {
            final OrderPosition position = new OrderPosition();
            position.setQuantity(quantity);
            position.setProduct(product);
            position.setProductId(orderDetails.getProductId());
            position.setPrice(price);
            position.setVariantId(orderDetails.getVariantId());
            position.setVariant(variant.orElse(null));
            positions.add(position);
        });
        final Order order = new Order();
        order.setPhone(orderDetails.getPhone());
        order.setCreated(new Date());
        order.setStatus(OrderStatus.WAITING_FOR_APPROVAL);
        order.setPositions(positions);
        order.setOneClick(true);
        positions.forEach(position -> position.setOrder(order));
        final Order savedOrder = coreDao.create(order);
        sendTelegramNotification(savedOrder);
        return order;
    }

    public List<Order> getPurchaseHistory(final HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Collections.emptyList();
        }
        final var phoneCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> SiteConstants.PHONE_COOKIE.equals(cookie.getName())).findFirst();
        if (phoneCookie.isPresent()) {
            return purchaseHistoryService.getPurchaseHistory(phoneCookie.get().getValue());
        } else {
            return Collections.emptyList();
        }
    }
    
    public Order addProductToCart(final CartProduct cartProduct, final HttpServletRequest request) throws Exception {
        Product product = coreDao.findById(cartProduct.getProductId(), Product.class);
        final var variant = getVariant(cartProduct.getVariantId(), product);
        final Order order = sessionService.getCurrentOrder(request);
        if (product != null) {
            PriceCalculator.breakQuantityByVolume(product, variant, cartProduct.getQuantity()).forEach((price, quantity) -> {
                OrderPosition position = new OrderPosition();
                position.setPositionId(UUID.randomUUID().toString());
                position.setOrder(order);
                position.setQuantity(quantity);
                position.setPrice(price);
                position.setProduct(product);
                position.setProductId(product.getId());
                position.setVariantId(cartProduct.getVariantId());
                position.setVariant(variant.orElse(null));
                order.getPositions().add(position);
            });
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        return order;
    }
    
    private Optional<ProductVariant> getVariant(final String variantId, final Product product) {
        if (variantId != null) {
            return productService.getVariants(product).stream()
                .filter(v -> v.getExternalId().equals(variantId)).findFirst();
        } else {
            return Optional.empty();
        }
    }
    
    private Address getOrderAddress(final OrderDetailsWrapper orderDetails) {
        final Address address = new Address();
        address.setRegion(orderDetails.getRegion());
        address.setDistrict(orderDetails.getDistrict());
        address.setCity(orderDetails.getCity());
        address.setHouse(orderDetails.getHouse());
        address.setStreet(orderDetails.getStreet());
        address.setPostcode(orderDetails.getPostcode());
        address.setFlat(orderDetails.getFlat());
        address.setFirstname(orderDetails.getFirstname());
        address.setLastname(orderDetails.getLastname());
        address.setMiddlename(orderDetails.getMiddlename());
        return address;
    }
    
    private EuropostLocationSnapshot getOrderEuropostLocation(final OrderDetailsWrapper orderDetails) throws Exception {
        final EuropostLocationSnapshot snapshot = new EuropostLocationSnapshot();
        final EuropostLocation location =
                coreDao.findById(orderDetails.getEuropostLocationId(), EuropostLocation.class);
        snapshot.setAddress(location.getAddress());
        snapshot.setAltId(location.getAltId());
        snapshot.setCity(location.getCity());
        snapshot.setExternalId(location.getExternalId());
        snapshot.setIsNew(location.getIsNew());
        snapshot.setLatitude(location.getLatitude());
        snapshot.setLongitude(location.getLongitude());
        snapshot.setNote(location.getNote());
        snapshot.setWarehouseId(location.getWarehouseId());
        snapshot.setWorkingHours(location.getWorkingHours());
        snapshot.setFirstname(orderDetails.getEuropostFirstname());
        snapshot.setLastname(orderDetails.getEuropostLastname());
        snapshot.setMiddlename(orderDetails.getEuropostMiddlename());
        return snapshot;
    }
    
    private void sendTelegramNotification(final Order order) {
        try {
            telegramBotOrderService.sendNewOrderNotification(order);
        } catch (Exception e) {
            LOG.error("Can't send Telegram notification for order: " + order.getId(), e);
        }
    }
}
