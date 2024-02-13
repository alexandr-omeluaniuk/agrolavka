package ss.agrolavka.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.dao.OrderDAO;
import ss.agrolavka.service.OrderDocumentService;
import ss.agrolavka.service.OrderService;
import ss.agrolavka.wrapper.OrderSearchRequest;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchResponse;

/**
 * Order REST controller.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/protected/order")
public class OrderRESTController {
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderRESTController.class);
    
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    /** Order DAO. */
    @Autowired
    private OrderDAO orderDAO;
    /** Order document service. */
    @Autowired
    private OrderDocumentService orderDocumentService;
    
    @Autowired
    private OrderService orderService;
    /**
     * Delete order.
     * @param id order ID.
     * @throws Exception error.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id)throws Exception {
        coreDAO.delete(id, Order.class);
    }
    /**
     * Get order.
     * @param id order ID.
     * @return order.
     * @throws Exception error.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Order get(@PathVariable("id") Long id)throws Exception {
        Order order = coreDAO.findById(id, Order.class);
        return order;
    }

    /**
     * Update order.
     * @param orderForm order form.
     * @throws Exception error.
     */
    @RequestMapping(method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateOrder(@RequestBody final Order orderForm) throws Exception {
        orderService.updateOrder(orderForm);
    }
    /**
     * Search orders.
     * @param page page number.
     * @param pageSize page size.
     * @param order order direction.
     * @param orderBy order by field.
     * @param status order status.
     * @param text search text.
     * @param showClosed show closed orders.
     * @return search response.
     * @throws Exception error.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntitySearchResponse search(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "order_by", required = false) String orderBy,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "show_closed", required = false) boolean showClosed
    ) throws Exception {
        OrderSearchRequest searchRequest = new OrderSearchRequest();
        searchRequest.setPage(page == null ? 1 : page);
        searchRequest.setPageSize(pageSize == null ? Integer.MAX_VALUE : pageSize);
        searchRequest.setOrder(order);
        searchRequest.setOrderBy(orderBy);
        searchRequest.setStatus(status);
        searchRequest.setText(text);
        searchRequest.setShowClosed(showClosed);
        return new EntitySearchResponse(orderDAO.count(searchRequest).intValue(), orderDAO.search(searchRequest));
    }
    
    @RequestMapping(value = "/print/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void printOrder(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        final Order order = getOrder(id);
        final byte[] pdf = orderDocumentService.generateOrderPdf(order);
        response.getOutputStream().write(pdf);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Заказ №" + order.getId() + ".pdf");
        response.addHeader("ContentType", "application/pdf");
        response.addHeader("Content-Length", pdf.length + "");
    }
    
    @RequestMapping(value = "/print-orders/{ids}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void printOrders(@PathVariable("ids") String ids, HttpServletResponse response) throws Exception {
        final List<Order> orders = new ArrayList<>();
        for (String id : ids.split("-")) {
            orders.add(getOrder(Long.valueOf(id)));
        }
        final byte[] pdf = orderDocumentService.generateOrdersPdf(orders);
        response.getOutputStream().write(pdf);
        response.addHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=orders-" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + ".pdf"
        );
        response.addHeader("ContentType", "application/pdf");
        response.addHeader("Content-Length", pdf.length + "");
    }
    
    private Order getOrder(final Long id) throws Exception {
        final Order order = coreDAO.findById(id, Order.class);
        for (OrderPosition pos : order.getPositions()) {
            pos.setProduct(coreDAO.findById(pos.getProductId(), Product.class));
        }
        LOG.debug(order.getAddress() + "");
        order.getPositions().forEach(position -> LOG.info(position.getProduct() + ""));
        return order;
    }
}
