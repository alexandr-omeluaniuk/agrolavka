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

import java.util.List;
import ss.entity.agrolavka.Order;

/**
 * Management of order documents.
 * @author alex
 */
public interface OrderDocumentService {
    
    /**
     * Generate order PDF.
     * @param order order.
     * @return PDF file.
     * @throws Exception any error.
     */
    byte[] generateOrderPdf(Order order) throws Exception;
    
    /**
     * Generate PDF for orders.
     * @param orders orders.
     * @return PDF file.
     * @throws Exception any error.
     */
    byte[] generateOrdersPdf(List<Order> orders) throws Exception;
    
}
