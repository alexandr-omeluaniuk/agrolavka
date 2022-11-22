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
package ss.agrolavka.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.service.GroupProductsService;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.Product;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.service.ImageService;

/**
 * Group products service implementation.
 * @author alex
 */
@Service
class GroupProductsServiceImpl implements GroupProductsService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(GroupProductsServiceImpl.class);
    
    private static final String VOLUME_VALUE_PATTERN = "[\\d|,|.]*л";
    private static final String VOLUME_TOKEN_PATTERN = ",\\s" + VOLUME_VALUE_PATTERN;
    private static final String VOLUMABLE_PRODUCT_NAME_PATTERN = "^(.)*" + VOLUME_TOKEN_PATTERN + "$";
    
    
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Image service. */
    @Autowired
    private ImageService imageService;

    @Override
    
    public void groupProductByVolumes() throws Exception {
        resetHiddenFlag();
        final Set<Long> hiddenProducts = doGrouping();
        setHiddenFlag(hiddenProducts);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private Set<Long> doGrouping() throws Exception {
        final List<Product> allProducts = coreDAO.getAll(Product.class);
        final Map<String, Product> productsMap = allProducts.stream()
                .collect(Collectors.toMap(Product::getName, (p) -> p));
        final Map<String, List<Product>> groups = grouping(allProducts);
        final Set<Long> hiddenProducts = new HashSet<>();
        for (String groupedProductName : groups.keySet()) {
            final List<Product> products = groups.get(groupedProductName);
            products.forEach(p -> hiddenProducts.add(p.getId()));
            Product newProduct = createProductsWithVolumes(products, groupedProductName);
            final List<EntityImage> images = new ArrayList<>();
            for (final EntityImage image : newProduct.getImages()) {
                image.setId(null);
                image.setData(imageService.readImageFromDisk(image));
                image.setFileNameOnDisk(null);
                image.setSize(0L);
                images.add(image);
            }
            if (productsMap.containsKey(groupedProductName)) {
                LOG.info("Update product with volumes: " + groupedProductName + ", size " + products.size());
                final Product existingProduct = productsMap.get(groupedProductName);
                existingProduct.setDescription(newProduct.getDescription());
                existingProduct.setImages(images);
                existingProduct.setMinPrice(newProduct.getMinPrice());
                existingProduct.setMaxPrice(newProduct.getMaxPrice());
                existingProduct.setPrice(newProduct.getPrice());
                existingProduct.setUpdated(new Date());
                existingProduct.setQuantity(newProduct.getQuantity());
                existingProduct.setBuyPrice(0d);
                existingProduct.setUrl(newProduct.getUrl());
                existingProduct.setVolumes(newProduct.getVolumes());
                existingProduct.setHidden(false);
                coreDAO.update(existingProduct);
            } else {
                LOG.info("New product with volumes: " + groupedProductName + ", size " + products.size());
                newProduct.setImages(null);
                newProduct = coreDAO.create(newProduct);
                newProduct.setImages(images);
                coreDAO.update(newProduct);
            }
        }
        return hiddenProducts;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void resetHiddenFlag() throws Exception {
        final List<Product> allProducts = coreDAO.getAll(Product.class);
        allProducts.forEach(p -> p.setHidden(false));
        coreDAO.massUpdate(allProducts);
        LOG.info("Flag 'hidden' has been reset");
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void setHiddenFlag(final Set<Long> ids) throws Exception {
        final List<Product> hiddenProducts = coreDAO.getAll(Product.class).stream()
                .filter(p -> ids.contains(p.getId()))
                .collect(Collectors.toList());
        hiddenProducts.forEach(p -> p.setHidden(true));
        coreDAO.massUpdate(hiddenProducts);
        LOG.info("Flag 'hidden' has been set");
    }
    
    private Map<String, List<Product>> grouping(final List<Product> products) {
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
    
    private Product createProductsWithVolumes(final List<Product> products, final String productName) {
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
                volumeObj.put(Product.VOLUME_KEY, volumeToken.replace(", ", "").trim());
                volumeObj.put(Product.PRICE_KEY, p.getPrice());
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
        product.setExternalId(GROUPED_PRODUCT_EXTERNAL_ID);
        product.setBuyPrice(0d);
        product.setUrl(UrlProducer.transliterate(product.getName()));
        product.setImages(images);
        product.setVolumes(new JSONArray(volumes).toString());
        product.setHidden(false);
        return product;
    }
    
}
