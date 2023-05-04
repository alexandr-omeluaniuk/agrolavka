/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import java.util.List;
import ss.agrolavka.wrapper.OrderSearchRequest;
import ss.entity.agrolavka.Order;

/**
 * Order DAO.
 * @author alex
 */
public interface OrderDAO {
    /**
     * Search orders.
     * @param request search order request.
     * @return list of orders.
     * @throws Exception error.
     */
    List<Order> search(OrderSearchRequest request) throws Exception;
    /**
     * Get count of orders.
     * @param request search order request.
     * @return number of total orders.
     * @throws Exception error.
     */
    Long count(OrderSearchRequest request) throws Exception;
}
