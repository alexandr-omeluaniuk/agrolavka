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
package ss.agrolavka.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import ss.entity.agrolavka.Product;

/**
 *
 * @author alex
 */
public class ProductGrouperTest {
    
    @Test
    public void testProductsGrouping() throws Exception {
        Map<String, List<Product>> result = ProductGrouper.grouping(Arrays.asList(
                createProduct(1L, "Балерина Форте гербицид, 0,1л"),
                createProduct(2L, "Балерина Форте гербицид, 1л"),
                createProduct(3L, "Балерина Форте гербицид, 10л"),
                createProduct(4L, "Балерина Форте гербицид, 5л"),
                createProduct(5L, "Балерина Форте гербицид, 5l"),
                createProduct(6L, "Форте гербицид, 5л"),
                createProduct(7L, "Балерина гербицид, 5л")
        ));
        result.keySet().forEach(key -> System.out.println(key));
        Assert.assertEquals(1, result.size());
    }
    
    private Product createProduct(final Long id, final String name) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        return product;
    }
    
}
