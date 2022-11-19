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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import ss.agrolavka.constants.SiteConstants;
import ss.entity.agrolavka.Product;
import ss.entity.martin.EntityImage;

/**
 * Products grouper.
 * @author alex
 */
public class ProductGrouper {
    
    private static final String VOLUME_VALUE_PATTERN = "[\\d|,|.]*л";
    private static final String VOLUME_TOKEN_PATTERN = ",\\s" + VOLUME_VALUE_PATTERN;
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
    
    public static Product createProductsWithVolumes(final List<Product> products, final String productName) {
        final Product product = new Product();
        product.setName(productName);
        String maxDescription = "";
        Double minPrice = Double.MAX_VALUE;
        Double maxPrice = 0d;
        Double quantity = 0d;
        List<EntityImage> images = new ArrayList<>();
        final List<JSONObject> volumes = new ArrayList<>();
        for (final Product p : products) {
            if (p.getDescription() != null && p.getDescription().length() > maxDescription.length()) {
                maxDescription = p.getDescription();
            }
            if (p.getPrice() > maxPrice) {
                maxPrice = p.getPrice();
            }
            if (p.getPrice() < minPrice) {
                minPrice = p.getPrice();
            }
            if (p.getQuantity() != null && p.getQuantity() > quantity) {
                quantity = p.getQuantity();
            }
            if (p.getImages() != null && p.getImages().size() > images.size()) {
                images = p.getImages();
            }
            final Pattern pattern = Pattern.compile("(" + VOLUME_TOKEN_PATTERN + ")");
            final Matcher matcher = pattern.matcher(p.getName());
            if (matcher.find()) {
                final String volumeToken = matcher.group();
                final JSONObject volumeObj = new JSONObject();
                volumeObj.put("v", volumeToken.replace(", ", "").trim());
                volumeObj.put("p", p.getPrice());
                volumes.add(volumeObj);
            }
            product.setGroup(p.getGroup());
            product.setPrice(p.getPrice());
        }
        product.setDescription(maxDescription);
        product.setMaxPrice(maxPrice);
        product.setMinPrice(minPrice);
        product.setPrice(minPrice);
        product.setUpdated(new Date());
        product.setQuantity(quantity);
        product.setExternalId(SiteConstants.PRODUCT_WITH_VOLUMES_EXTERNAL_ID);
        product.setBuyPrice(0d);
        product.setUrl(UrlProducer.transliterate(product.getName()));
        product.setImages(images);
        Collections.sort(volumes, (item1, item2) -> {
            final Double price1 = item1.getDouble("p");
            final Double price2 = item2.getDouble("p");
            if (price1 > price2) {
                return 1;
            } else if (price1 < price2) {
                return -1;
            } else {
                return 0;
            }
        });
        product.setVolumes(new JSONArray(volumes).toString());
        product.setHidden(false);
        return product;
    }
    
}
