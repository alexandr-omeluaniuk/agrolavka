/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.ImageStubs;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.util.UrlProducer;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Address;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.service.FirebaseClient;
import ss.martin.platform.wrapper.PushNotification;

/**
 * Public REST controller.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/public")
class AgrolavkaPublicRESTController {
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    /** Firebase client. */
    @Autowired
    private FirebaseClient firebaseClient;
    /**
     * Get product image.
     * @param id product ID.
     * @return product image.
     * @throws Exception error.
     */
    @RequestMapping(value = "/product-image/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Transactional(propagation = Propagation.SUPPORTS)
    public byte[] getProductImage(@PathVariable("id") Long id) throws Exception {
        Product product = coreDAO.findById(id, Product.class);
        return product != null && !product.getImages().isEmpty() ? product.getImages().get(0).getData()
                : Base64.getDecoder().decode(ImageStubs.NO_PRODUCT_IMAGE);
    }
    /**
     * Get products group image.
     * @param id products group ID.
     * @return products group image.
     * @throws Exception error.
     */
    @RequestMapping(value = "/products-group-image/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Transactional(propagation = Propagation.SUPPORTS)
    public byte[] getProductsGroupImage(@PathVariable("id") Long id) throws Exception {
        ProductsGroup productsGroup = coreDAO.findById(id, ProductsGroup.class);
        return productsGroup != null && !productsGroup.getImages().isEmpty() ? productsGroup.getImages().get(0).getData()
                : Base64.getDecoder().decode(ImageStubs.NO_PRODUCT_IMAGE);
    }
    /**
     * Search product.
     * @param searchText search text.
     * @return products.
     * @throws Exception error.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> search(@RequestParam(value = "searchText", required = false) String searchText)
            throws Exception {
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(20);
        request.setText(searchText);
        request.setOrder("asc");
        request.setOrderBy("name");
        Map<String, Object> result = new HashMap<>();
        List<Product> list = productDAO.search(request);
        for (Product product : list) {
            product.setBuyPrice(null);
            product.setUrl(UrlProducer.buildProductUrl(product));
        }
        result.put("data", list);
        result.put("count", productDAO.count(request));
        return result;
    }
    /**
     * Add product to cart.
     * @param id product ID.
     * @param request HTTP request.
     * @return cart.
     * @throws Exception error.
     */
    @RequestMapping(value = "/cart/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order addToCart(@PathVariable("id") Long id, HttpServletRequest request) throws Exception {
        Product product = coreDAO.findById(id, Product.class);
        Order order = (Order) request.getSession(true).getAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        if (product != null) {
            OrderPosition position = new OrderPosition();
            position.setOrder(order);
            position.setPrice(product.getDiscountPrice());
            position.setQuantity(1);
            position.setProduct(product);
            position.setProductId(product.getId());
            order.getPositions().add(position);
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        return order;
    }
    /**
     * Remove product from cart.
     * @param id product ID.
     * @param request HTTP request.
     * @return cart.
     * @throws Exception error.
     */
    @RequestMapping(value = "/cart/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order removeFromCart(@PathVariable("id") Long id, HttpServletRequest request) throws Exception {
        Order order = (Order) request.getSession(true).getAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        Set<OrderPosition> positions = order.getPositions().stream().filter(pos -> {
            return !Objects.equals(pos.getProductId(), id);
        }).collect(Collectors.toSet());
        order.setPositions(positions);
        request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        return order;
    }
    /**
     * Change cart position quantity.
     * @param id product ID.
     * @param quantity quantity.
     * @param request HTTP request.
     * @return cart.
     * @throws Exception error.
     */
    @RequestMapping(value = "/cart/quantity/{id}/{quantity}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order changeCartPositionQuantity(@PathVariable("id") Long id, @PathVariable("quantity") Integer quantity,
            HttpServletRequest request) throws Exception {
        Order order = (Order) request.getSession(true).getAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        OrderPosition position = order.getPositions().stream().filter(pos -> {
            return Objects.equals(pos.getProductId(), id);
        }).findFirst().get();
        position.setQuantity(quantity > 0 ? quantity : 1);
        request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        return order;
    }
    /**
     * Confirm order.
     * @param request HTTP request.
     * @param formValues form values.
     * @return order.
     * @throws Exception error.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RequestMapping(value = "/order", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order confirmOrder(HttpServletRequest request, @RequestBody() Map<String, Object> formValues)
            throws Exception {
        final Order order = (Order) request.getSession(true).getAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        Double total = 0d;
        for (OrderPosition pos : order.getPositions()) {
            total += pos.getQuantity() * pos.getPrice();
        }
        order.setPhone((String) formValues.get("phone"));
        order.setCreated(new Date());
        order.setStatus(OrderStatus.WAITING_FOR_APPROVAL);
        if (formValues.containsKey("city") && !formValues.get("city").toString().isBlank()) {
            Address address = new Address();
            address.setCity((String) formValues.get("city"));
            address.setHouse((String) formValues.get("house"));
            address.setStreet((String) formValues.get("street"));
            address.setPostcode((String) formValues.get("postcode"));
            address.setFlat((String) formValues.get("flat"));
            order.setAddress(address);
        }
        Order savedOrder = coreDAO.create(order);
        Order neworder = new Order();
        neworder.setPositions(new HashSet<>());
        request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, neworder);
        PushNotification notification = new PushNotification();
        notification.setTitle("Поступил новый заказ");
        notification.setBody("Потенциальная сумма заказа - " + String.format("%.2f", total)
                + " BYN. Номер заказа: " + savedOrder.getId());
        notification.setTtlInSeconds(String.valueOf(TimeUnit.DAYS.toSeconds(1)));
        notification.setIcon("https://agrolavka.by/favicon.svg");
        notification.setClickAction("https://agrolavka.by/admin/app/agrolavka/order/" + savedOrder.getId());
        notification.setClickActionLabel("Открыть");
        firebaseClient.sendTopicNotification(notification, SiteConstants.FIREBASE_TOPIC_ORDER_CREATED);
        return savedOrder;
    }
}
