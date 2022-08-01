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
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ss.agrolavka.SpringConfig;
import ss.entity.agrolavka.Order;

/**
 *
 * @author alex
 */
@SpringBootTest(classes = { SpringConfig.class }, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrderDocumentServiceTest {
    
    @Autowired
    private OrderDocumentService orderDocumentService;
    
    @Test
    public void testGenerateOrderPdf() throws Exception {
        // Given
        final Order order = new Order();
        order.setId(1234L);
        order.setPositions(new HashSet<>());
        order.setComment("Some comment from owner...");
        
        // When
        final byte[] result = orderDocumentService.generateOrderPdf(order);
        
        // Then
        Files.write(new File("/home/alex/Downloads/test.pdf").toPath(), result, StandardOpenOption.CREATE);
    }
    
}
