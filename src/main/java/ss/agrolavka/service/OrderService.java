/*
 * Copyright (C) 2022 alex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ss.agrolavka.service;

import javax.servlet.http.HttpServletRequest;
import ss.agrolavka.wrapper.CartProduct;
import ss.agrolavka.wrapper.OneClickOrderWrapper;
import ss.agrolavka.wrapper.OrderDetailsWrapper;
import ss.entity.agrolavka.Order;

/**
 * Order service.
 * @author alex
 */
public interface OrderService {
    /**
     * Create order.
     * @param order order.
     * @param orderDetails order details.
     * @return new order.
     * @throws Exception exception.
     */
    Order createOrder(Order order, OrderDetailsWrapper orderDetails) throws Exception;
    /**
     * Create one click order.
     * @param orderDetails order details.
     * @return order.
     * @throws Exception error.
     */
    Order createOneClickOrder(OneClickOrderWrapper orderDetails) throws Exception;
    /**
     * Get current order.
     * @param request HTTP request.
     * @return order.
     */
    Order getCurrentOrder(HttpServletRequest request);
    /**
     * Add product to cart
     * @param cartProduct cart product form.
     * @param request HTTP request.
     * @return current order.
     * @throws Exception exception.
     */
    Order addProductToCart(CartProduct cartProduct, HttpServletRequest request) throws Exception;
}
