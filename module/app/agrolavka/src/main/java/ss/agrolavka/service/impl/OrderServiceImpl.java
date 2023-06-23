package ss.agrolavka.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.service.OrderService;
import ss.agrolavka.util.PriceCalculator;
import ss.agrolavka.wrapper.CartProduct;
import ss.agrolavka.wrapper.OneClickOrderWrapper;
import ss.agrolavka.wrapper.OrderDetailsWrapper;
import ss.entity.agrolavka.Address;
import ss.entity.agrolavka.EuropostLocation;
import ss.entity.agrolavka.EuropostLocationSnapshot;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.core.dao.CoreDao;

/**
 * Order service implementation.
 * @author alex
 */
@Service
class OrderServiceImpl implements OrderService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    
    @Autowired
    private TelegramBotsService telegramBotsService;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Order createOrder(final Order order, final OrderDetailsWrapper orderDetails) throws Exception {
        LOG.info("----------------------------------- Create new order ----------------------------------------------");
        final Double total = order.getPositions().stream().mapToDouble(e -> e.getPrice() * e.getQuantity())
                .reduce(0, (a, b) -> a + b);
        LOG.info("Order total [" + total + "]");
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
        final Order savedOrder = coreDAO.create(order);
        sendTelegramNotification(savedOrder, total);
        LOG.info("---------------------------------------------------------------------------------------------------");
        return savedOrder;
    }
    
    @Override
    public Order createOneClickOrder(final OneClickOrderWrapper orderDetails) throws Exception {
        final Product product = coreDAO.findById(orderDetails.getProductId(), Product.class);
        final List<OrderPosition> positions = new ArrayList<>();
        PriceCalculator.breakQuantityByVolume(product, orderDetails.getQuantity()).forEach((price, quantity) -> {
            final OrderPosition position = new OrderPosition();
            position.setQuantity(quantity);
            position.setProduct(product);
            position.setProductId(orderDetails.getProductId());
            position.setPrice(price);
            positions.add(position);
        });
        final Double total = positions.stream().map(pos -> pos.getQuantity() * pos.getPrice()).reduce(0d, Double::sum);
        final Order order = new Order();
        order.setPhone(orderDetails.getPhone());
        order.setCreated(new Date());
        order.setStatus(OrderStatus.WAITING_FOR_APPROVAL);
        order.setPositions(positions);
        order.setOneClick(true);
        positions.forEach(position -> position.setOrder(order));
        final Order savedOrder = coreDAO.create(order);
        sendTelegramNotification(savedOrder, total);
        return order;
    }
    
    @Override
    public Order getCurrentOrder(final HttpServletRequest request) {
        Order order = (Order) request.getSession(true).getAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        if (order == null) {
            order = new Order();
            order.setPositions(new ArrayList<>());
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        return order;
    }
    
    @Override
    public Order addProductToCart(final CartProduct cartProduct, final HttpServletRequest request) throws Exception {
        Product product = coreDAO.findById(cartProduct.getProductId(), Product.class);
        final Order order = getCurrentOrder(request);
        if (product != null) {
            PriceCalculator.breakQuantityByVolume(product, cartProduct.getQuantity()).forEach((price, quantity) -> {
                OrderPosition position = new OrderPosition();
                position.setPositionId(UUID.randomUUID().toString());
                position.setOrder(order);
                position.setQuantity(quantity);
                position.setPrice(price);
                position.setProduct(product);
                position.setProductId(product.getId());
                order.getPositions().add(position);
            });
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        return order;
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
                coreDAO.findById(orderDetails.getEuropostLocationId(), EuropostLocation.class);
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
    
    private void sendTelegramNotification(final Order order, final Double total) {
        try {
            telegramBotsService.sendNewOrderNotification(order, total);
        } catch (Exception e) {
            LOG.error("Can't send Telegram notification for order: " + order.getId(), e);
        }
    }
}
