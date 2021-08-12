/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.dao.OrderDAO;
import ss.agrolavka.wrapper.OrderSearchRequest;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.wrapper.EntitySearchResponse;

/**
 * Order REST controller.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/protected/order")
public class OrderRESTController {
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Order DAO. */
    @Autowired
    private OrderDAO orderDAO;
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
     * Get order positions.
     * @param id order ID.
     * @return order positions.
     * @throws Exception error.
     */
    @RequestMapping(value = "/positions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<OrderPosition> getPositions(@PathVariable("id") Long id) throws Exception {
        Order order = coreDAO.findById(id, Order.class);
        for (OrderPosition pos : order.getPositions()) {
            pos.setProduct(coreDAO.findById(pos.getProductId(), Product.class));
        }
        return order.getPositions();
    }
    /**
     * Change order status.
     * @param id order ID.
     * @param status status
     * @throws Exception error.
     */
    @RequestMapping(value = "/status/{id}/{status}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeOrderStatus(@PathVariable("id") Long id, @PathVariable("status") String status) throws Exception {
        Order order = coreDAO.findById(id, Order.class);
        order.setStatus(OrderStatus.valueOf(status));
        coreDAO.update(order);
    }
    /**
     * Search orders.
     * @param page page number.
     * @param pageSize page size.
     * @param order order direction.
     * @param orderBy order by field.
     * @param status order status.
     * @param text search text.
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
            @RequestParam(value = "text", required = false) String text
    ) throws Exception {
        OrderSearchRequest searchRequest = new OrderSearchRequest();
        searchRequest.setPage(page == null ? 1 : page);
        searchRequest.setPageSize(pageSize == null ? Integer.MAX_VALUE : pageSize);
        searchRequest.setOrder(order);
        searchRequest.setOrderBy(orderBy);
        searchRequest.setStatus(status);
        searchRequest.setText(text);
        EntitySearchResponse response = new EntitySearchResponse();
        response.setData(orderDAO.search(searchRequest));
        response.setTotal(orderDAO.count(searchRequest).intValue());
        return response;
    }
}
