/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.OrderService;
import ss.agrolavka.util.UrlProducer;
import ss.agrolavka.wrapper.OrderDetailsWrapper;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Feedback;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.platform.dao.CoreDAO;

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
    public Map<String, Object> search(@RequestParam(value = "searchText", required = false) String searchText)
            throws Exception {
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(100);
        request.setText(searchText);
        request.setOrder("asc");
        request.setOrderBy("name");
        Map<String, Object> result = new HashMap<>();
        List<Product> list = productDAO.search(request);
        for (Product product : list) {
            product.setBuyPrice(null);
            product.setQuantity(product.getQuantity() != null && product.getQuantity() > 0 ? 1d : 0d);
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
        final Order order = (Order) request.getSession(true).getAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        final Order savedOrder = orderService.createOrder(order, orderWrapper);
        final Order newOrder = new Order();
        newOrder.setPositions(new HashSet<>());
        request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, newOrder);
        return savedOrder;
    }
}
