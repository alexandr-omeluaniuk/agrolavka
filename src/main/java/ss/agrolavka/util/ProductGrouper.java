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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import ss.entity.agrolavka.Product;

/**
 * Products grouper.
 * @author alex
 */
public class ProductGrouper {
    
    private static final String VOLUME_TOKEN_PATTERN = ",\\s(\\d|,|.)*л";
    private static final String VOLUMABLE_PRODUCT_NAME_PATTERN = "^(.)*" + VOLUME_TOKEN_PATTERN + "$";
    
    public static Map<String, List<Product>> grouping(final List<Product> products) {
        final Map<String, List<Product>> result = new HashMap<>();
        final Set<Long> forGrouping = products.stream()
                .filter(product -> Pattern.matches(VOLUMABLE_PRODUCT_NAME_PATTERN, product.getName()))
                .map(product -> product.getId()).collect(Collectors.toSet());
        products.stream().filter(product -> forGrouping.contains(product.getId())).forEach(product -> {
            final String name = product.getName();
            final Pattern pattern = Pattern.compile("(" + VOLUME_TOKEN_PATTERN + ")");
            final Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                final String volumeUnitToken = matcher.group();
                final String commonProductName = name.substring(0, name.lastIndexOf(volumeUnitToken));
                if (!result.containsKey(commonProductName)) {
                    result.put(commonProductName, new ArrayList<>());
                }
                result.get(commonProductName).add(product);
            }
        });
        final Set<String> keysForRemove = result.keySet().stream().filter(key -> result.get(key).size() == 1)
                .collect(Collectors.toSet());
        keysForRemove.forEach(key -> result.remove(key));
        return result;
    }
    
}
