/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.agrolavka.Shop;
import ss.entity.agrolavka.Slide;

/**
 * Application cache.
 * @author alex
 */
public class AppCache {
    /** Group parents map. Key is group external ID, value is parent group. */
    private static final Map<String, ProductsGroup> GROUPS_PARENT_MAP = new HashMap<>();
    /** All groups. */
    private static final List<ProductsGroup> ALL_GROUPS = new ArrayList<>();
    /** New products. */
    private static List<Product> newProducts = null;
    /** Product with discounts. */
    private static List<Product> productWithDiscounts = null;
    /** Products count. */
    private static Long productsCount = null;
    /** All shops. */
    private static List<Shop> allShops = null;
    /** Slides. */
    private static List<Slide> allSlides = null;
    /**
     * Update catalog data.
     * @param groups product groups.
     */
    public static synchronized void flushCache(List<ProductsGroup> groups) {
        newProducts = null;
        productWithDiscounts = null;
        GROUPS_PARENT_MAP.clear();
        ALL_GROUPS.clear();
        ALL_GROUPS.addAll(groups);
        //List<ProductsGroup> roots = new ArrayList<>();
        Map<String, ProductsGroup> externalIdsMap = new HashMap<>();
        for (ProductsGroup group : groups) {
            externalIdsMap.put(group.getExternalId(), group);
        }
        for (ProductsGroup group : groups) {
            String parentId = group.getParentId();
            GROUPS_PARENT_MAP.put(group.getExternalId(), parentId == null ? null : externalIdsMap.get(parentId));
        }
    }
    /**
     * Flush shops cache.
     * @param shops actual shops.
     */
    public static synchronized void flushShopsCache(final List<Shop> shops) {
        allShops = shops;
    }
    /**
     * Flush slides cache.
     * @param slides actual slides.
     */
    public static synchronized void flushSlidesCache(final List<Slide> slides) {
        allSlides = slides;
    }
    /**
     * Get new products.
     * @return new products.
     */
    public static synchronized List<Product> getNewProducts() {
        return newProducts;
    }
    /**
     * Get products with discounts.
     * @return  products with discounts.
     */
    public static synchronized List<Product> getProductsWithDiscounts() {
        return productWithDiscounts;
    }
    /**
     * Set new products to cache.
     * @param products new products.
     */
    public static synchronized void setNewProducts(List<Product> products) {
        newProducts = products;
    }
    /**
     * Set products with discounts.
     * @param products products with discounts.
     */
    public static synchronized void setProductsWithDiscounts(List<Product> products) {
        productWithDiscounts = products;
    }
    /**
     * Get products count.
     * @return products count.
     */
    public static synchronized Long getProductsCount() {
        return productsCount;
    }
    /**
     * Set products count to cache.
     * @param count products count.
     */
    public static synchronized void setProductsCount(Long count) {
        productsCount = count;
    }
    /**
     * Get top categories.
     * @return top categories.
     */
    public static synchronized List<ProductsGroup> getTopCategories() {
        List<ProductsGroup> topCategories = ALL_GROUPS.stream().filter(group -> {
            return group.isTopCategory() != null && group.isTopCategory();
        }).collect(Collectors.toList());
        Collections.sort(topCategories);
        return topCategories;
    }
    /**
     * Get categories tree.
     * @return categories tree.
     */
    public static synchronized Map<String, List<ProductsGroup>> getCategoriesTree() {
        Map<String, List<ProductsGroup>> tree = new HashMap<>();
        for (ProductsGroup group : ALL_GROUPS) {
            final String parentId = group.getParentId() != null ? group.getParentId() : "-1";
            if (!tree.containsKey(parentId)) {
                tree.put(parentId, new ArrayList<>());
            }
            tree.get(parentId).add(group);
        }
        return tree;
    }
    /**
     * Get product groups.
     * @return product groups.
     */
    public static synchronized List<ProductsGroup> getProductsGroups() {
        return ALL_GROUPS;
    }
    /**
     * Get product groups tree.
     * @return product groups tree.
     */
    public static synchronized Map<String, ProductsGroup> getProductsGroupsTree() {
        return GROUPS_PARENT_MAP;
    }
    
    public static synchronized List<Shop> getShops() {
        return allShops;
    }
    public static synchronized List<Slide> getSlides() {
        return allSlides;
    }
    /**
     * Get root product groups.
     * @return root product groups.
     */
    public static synchronized List<ProductsGroup> getRootProductGroups() {
        return ALL_GROUPS.stream().filter(group -> {
            return group.getParentId() == null;
        }).collect(Collectors.toList());
    }
    
    public static int countGroupProducts(ProductsGroup group) {
        int total = 0;
        // TODO: calculate
        return total;
    }
    
    public static int countAvailableGroupProducts(ProductsGroup group) {
        int total = 0;
        // TODO: calculate
        return total;
    }
    public static boolean isBelongsToGroup(final String groupName, final ProductsGroup group) {
        if (group == null) {
            return false;
        }
        final var tree = AppCache.getProductsGroupsTree();
        var currentGroup = group;
        while (currentGroup != null) {
            if (groupName.equals(currentGroup.getName())) {
                return true;
            }
            currentGroup = tree.get(currentGroup.getExternalId());
        }
        return false;
    }
}
