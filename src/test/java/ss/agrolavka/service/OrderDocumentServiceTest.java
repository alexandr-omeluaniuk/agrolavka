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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ss.agrolavka.SpringConfig;
import ss.entity.agrolavka.Address;
import ss.entity.agrolavka.Discount;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;

/**
 *
 * @author alex
 */
@SpringBootTest(classes = { SpringConfig.class }, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrderDocumentServiceTest {
    
    @Autowired
    private OrderDocumentService orderDocumentService;
    
    //@Test
    public void testGenerateOrderPdf() throws Exception {
        // Given
        final Order order = new Order();
        order.setId(1234L);
        order.setPositions(new ArrayList<>());
        order.setAdminComment("Some comment from owner...");
        order.setComment("Some coomment from client");
        order.setPhone("+375 29 778 77 77");
        
        final Address address = new Address();
        address.setCity("Brest");
        address.setStreet("Rabinovaya");
        address.setDistrict("Brestsky");
        address.setFlat("52");
        address.setHouse("31");
        address.setRegion("Brestskaya obl");
        address.setPostcode("224033");
        address.setFirstname("Mike");
        address.setLastname("Ivanov");
        address.setMiddlename("Ivanivich");
        order.setAddress(address);
        
        final Discount discount = new Discount();
        discount.setDiscount(5d);
        
        final Product product = new Product();
        product.setName("Product #1");
        product.setDiscount(discount);
        
        final OrderPosition position = new OrderPosition();
        position.setPrice(20.53d);
        position.setQuantity(1);
        position.setProduct(product);
        
        order.getPositions().add(position);
        
        // When
        final byte[] result = orderDocumentService.generateOrderPdf(order);
        
        // Then
        Files.write(new File("/home/alex/Downloads/test.pdf").toPath(), result, StandardOpenOption.CREATE);
    }
    
}
