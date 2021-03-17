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

/**
 * URL producer.
 * Works like cache also.
 * @author alex
 */
public class UrlProducer {
    /** Group parents map. Key is group external ID, value is parent group. */
    private static final Map<String, ProductsGroup> GROUPS_PARENT_MAP = new HashMap<>();
    /** All groups. */
    private static final List<ProductsGroup> ALL_GROUPS = new ArrayList<>();
    /**
     * Update catalog data.
     * @param groups product groups.
     */
    public static synchronized void updateCatalog(List<ProductsGroup> groups) {
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
            if (group.getParentId() != null) {
                if (!tree.containsKey(group.getParentId())) {
                    tree.put(group.getParentId(), new ArrayList<>());
                }
                tree.get(group.getParentId()).add(group);
            }
        }
        return tree;
    }
    /**
     * Build product group URL.
     * @param group product group.
     * @return full URL.
     */
    public static synchronized String buildProductGroupUrl(ProductsGroup group) {
        StringBuilder sb = new StringBuilder("/catalog");
        List<String> parts = new ArrayList<>();
        ProductsGroup current = group;
        while (current != null) {
            parts.add(current.getUrl());
            current = GROUPS_PARENT_MAP.get(current.getExternalId());
        }
        Collections.reverse(parts);
        parts.forEach(token -> {
            sb.append("/").append(token);
        });
        return sb.toString();
    }
    /**
     * Build product URL.
     * @param product product.
     * @return full URL.
     */
    public static synchronized String buildProductUrl(Product product) {
        return buildProductGroupUrl(product.getGroup()) + "/" + product.getUrl();
    }
    /**
     * Get breadcrumbs path.
     * @param group leaf group.
     * @return path.
     * @throws Exception error. 
     */
    public static synchronized List<ProductsGroup> getBreadcrumbPath(ProductsGroup group) throws Exception {
        List<ProductsGroup> path = new ArrayList<>();
        ProductsGroup current = group;
        while (current != null) {
            path.add(current);
            final String parentId = current.getParentId();
            if (parentId != null) {
                current = ALL_GROUPS.stream().filter(g -> {
                    return parentId.equals(g.getExternalId());
                }).findFirst().get();
            } else {
                current = null;
            }
        }
        Collections.reverse(path);
        return path;
    }
    /**
     * Get product groups.
     * @return product groups.
     */
    public static synchronized List<ProductsGroup> getProductsGroups() {
        return ALL_GROUPS;
    }
}
