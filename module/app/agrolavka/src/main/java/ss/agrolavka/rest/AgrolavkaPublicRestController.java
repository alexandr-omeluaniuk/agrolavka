package ss.agrolavka.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.service.OrderService;
import ss.agrolavka.service.ProductService;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.wrapper.CartProduct;
import ss.agrolavka.wrapper.OneClickOrderWrapper;
import ss.agrolavka.wrapper.OrderDetailsWrapper;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.entity.agrolavka.Feedback;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.core.dao.CoreDao;

/**
 * Public REST controller.
 * @author alex
 */
@RestController
@RequestMapping(SiteConstants.URL_PUBLIC)
class AgrolavkaPublicRestController {
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    
    @Autowired
    private ProductService productService;
    /** Order service. */
    @Autowired
    private OrderService orderService;
    
    /**
     * Search product.
     * @param searchText search text.
     * @return products.
     * @throws Exception error.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductsSearchResponse search(
        @RequestParam(value = "searchText", required = false) final String searchText
    ) {
        return productService.quickSearchProducts(searchText);
    }
    
    /**
     * Add product to cart.
     * @param id product ID.
     * @param request HTTP request.
     * @return cart.
     * @throws Exception error.
     */
    @RequestMapping(value = "/cart", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order addToCart(
            @RequestBody final CartProduct cartProduct,
            final HttpServletRequest request
    ) throws Exception {
        return orderService.addProductToCart(cartProduct, request);
    }
    /**
     * Remove product from cart.
     * @param id product ID.
     * @param request HTTP request.
     * @return cart.
     * @throws Exception error.
     */
    @RequestMapping(value = "/cart/product/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order removeFromCartByProductId(@PathVariable("id") Long id, HttpServletRequest request) throws Exception {
        final Order order = orderService.getCurrentOrder(request);
        List<OrderPosition> positions = order.getPositions().stream().filter(pos -> {
            return !Objects.equals(pos.getProductId(), id);
        }).collect(Collectors.toList());
        order.setPositions(positions);
        if (positions.isEmpty()) {
            request.getSession().removeAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        } else {
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        return order;
    }
    
    @RequestMapping(value = "/cart/position/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order removeFromCartByPositionId(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
        final Order order = orderService.getCurrentOrder(request);
        List<OrderPosition> positions = order.getPositions().stream().filter(pos -> {
            return !Objects.equals(pos.getPositionId(), id);
        }).collect(Collectors.toList());
        order.setPositions(positions);
        if (positions.isEmpty()) {
            request.getSession().removeAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        } else {
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        return order;
    }
    /**
     * Change cart position quantity.
     * @param id position ID.
     * @param quantity quantity.
     * @param request HTTP request.
     * @return cart.
     * @throws Exception error.
     */
    @RequestMapping(value = "/cart/quantity/{id}/{quantity}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order changeCartPositionQuantity(@PathVariable("id") String id, @PathVariable("quantity") Integer quantity,
            HttpServletRequest request) throws Exception {
        final Order order = orderService.getCurrentOrder(request);
        final List<OrderPosition> positions = order.getPositions().stream().filter(pos -> {
            return Objects.equals(pos.getPositionId(), id);
        }).collect(Collectors.toList());
        if (!positions.isEmpty()) {
            positions.get(0).setQuantity(quantity > 0 ? quantity : 1);
        }
        request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        return order;
    }
    /**
     * Save feedback.
     * @param formValues form values.
     * @throws Exception error.
     */
    @RequestMapping(value = "/feedback", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Feedback saveFeedback(@RequestBody() Map<String, Object> formValues) throws Exception {
        Feedback feedback = new Feedback();
        feedback.setContact((String) formValues.get("contact"));
        feedback.setMessage((String) formValues.get("message"));
        feedback.setCreated(new Date());
        return coreDAO.create(feedback);
    }
    /**
     * Confirm order.
     * @param request HTTP request.
     * @param orderWrapper form values.
     * @return order.
     * @throws Exception error.
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order confirmOrder(HttpServletRequest request, @RequestBody() OrderDetailsWrapper orderWrapper)
            throws Exception {
        final Order order = orderService.getCurrentOrder(request);
        final Order savedOrder = orderService.createOrder(order, orderWrapper);
        final Order newOrder = new Order();
        newOrder.setPositions(new ArrayList<>());
        request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, newOrder);
        return savedOrder;
    }
    /**
     * Confirm one-click order.
     * @param orderWrapper order form.
     * @return order.
     * @throws Exception error.
     */
    @RequestMapping(value = "/order/one-click", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order confirmOneClickOrder(@RequestBody() OneClickOrderWrapper orderWrapper) throws Exception {
        return orderService.createOneClickOrder(orderWrapper);
    }
    
    @RequestMapping(value = "/catalog", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<ProductsGroup>> catalog() throws Exception {
        return AppCache.getCategoriesTree();
    }
}