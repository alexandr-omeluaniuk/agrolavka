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
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.OrderStatus;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.platform.dao.CoreDAO;

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
}
