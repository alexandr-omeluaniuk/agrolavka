package ss.agrolavka.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.service.OrderService;
import ss.agrolavka.service.ProductService;
import ss.agrolavka.service.SessionService;
import ss.agrolavka.service.VATSService;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.wrapper.CartProduct;
import ss.agrolavka.wrapper.OneClickOrderWrapper;
import ss.agrolavka.wrapper.OrderDetailsWrapper;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.entity.agrolavka.*;
import ss.martin.core.dao.CoreDao;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Public REST controller.
 * @author alex
 */
@RestController
@RequestMapping(SiteUrls.URL_PUBLIC)
class AgrolavkaPublicRestController {
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    
    @Autowired
    private ProductService productService;
    
    /** Order service. */
    @Autowired
    private OrderService orderService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private VATSService vatsService;

    @Autowired
    private ObjectMapper objectMapper;
    
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

    @RequestMapping(value = "/cart", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order addToCart(
            @RequestBody final CartProduct cartProduct,
            final HttpServletRequest request
    ) throws Exception {
        if (Objects.equals(cartProduct.getVariantId(), ProductVariant.PRIMARY_VARIANT)) {
            cartProduct.setVariantId(null);
        }
        return orderService.addProductToCart(cartProduct, request);
    }

    @RequestMapping(value = "/cart/product/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order removeFromCartByProductId(@PathVariable("id") Long id, HttpServletRequest request) {
        return removePosition((pos) -> !Objects.equals(pos.getProductId(), id), request);
    }
    
    @RequestMapping(value = "/cart/variant/{productId}/{variantId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order removeFromCartByVariantId(
            @PathVariable("productId") final Long productId,
            @PathVariable("variantId") final String variantId,
            HttpServletRequest request
    ) {
        if (ProductVariant.PRIMARY_VARIANT.equals(variantId)) {
            return removeFromCartByProductId(productId, request);
        } else {
            return removePosition((pos) -> !Objects.equals(pos.getVariantId(), variantId), request);
        }
    }
    
    @RequestMapping(value = "/cart/position/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Order removeFromCartByPositionId(@PathVariable("id") final String id, HttpServletRequest request) throws Exception {
        return removePosition((pos) -> !Objects.equals(pos.getPositionId(), id), request);
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
        final Order order = sessionService.getCurrentOrder(request);
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
    public Order confirmOrder(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody() OrderDetailsWrapper orderWrapper
    ) throws Exception {
        final Order order = sessionService.getCurrentOrder(request);
        final Order savedOrder = orderService.createOrder(order, orderWrapper);
        sessionService.setPhoneCookie(response, orderWrapper.getPhone());
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
    public Order confirmOneClickOrder(
        HttpServletResponse response,
        @RequestBody() OneClickOrderWrapper orderWrapper
    ) throws Exception {
        final var order = orderService.createOneClickOrder(orderWrapper);
        sessionService.setPhoneCookie(response, orderWrapper.getPhone());
        return order;
    }
    
    @RequestMapping(value = "/catalog", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<ProductsGroup>> catalog() throws Exception {
        return AppCache.getCategoriesTree();
    }

    @RequestMapping(value = "/vats", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String vatsIncome(
        @RequestBody String request
    ) throws Exception {
        final var response = vatsService.handleIncomingRequest(parse(request));
        return response == null ? "" : objectMapper.writeValueAsString(response);
    }

    @RequestMapping(value = "/phone-api", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String phoneApiCall(
            @RequestBody String request
    ) throws Exception {
        vatsService.phoneApiCall(request);
        return null;
    }

    private Map<String, String> parse(String request) {
        final var result = new HashMap<String, String>();
        Arrays.stream(request.split("&")).forEach(row -> {
            final var parts = row.split("=");
            result.put(parts[0], parts.length > 1 ? parts[1] : null);
        });
        return result;
    }
    
    private Order removePosition(Predicate<OrderPosition> predicate, HttpServletRequest request) {
        final Order order = sessionService.getCurrentOrder(request);
        List<OrderPosition> positions = order.getPositions().stream().filter(predicate).collect(Collectors.toList());
        order.setPositions(positions);
        if (positions.isEmpty()) {
            request.getSession().removeAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        } else {
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        return order;
    }
}
