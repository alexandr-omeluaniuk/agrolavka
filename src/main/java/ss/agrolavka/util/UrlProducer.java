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
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.DataModel;

/**
 * URL producer.
 * @author alex
 */
public class UrlProducer {
    /** Group parents map. Key is group external ID, value is parent group. */
    private static final Map<String, ProductsGroup> GROUPS_PARENT_MAP = new HashMap<>();
    /** All groups. */
    private static final List<ProductsGroup> ALL_GROUPS = new ArrayList<>();
    
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
     * Resolve URL to entity.
     * @param url url.
     * @return Product or ProductGroup entity.
     */
    public static synchronized DataModel resolveUrlToEntity(String url) {
        String last = url.substring(url.lastIndexOf("/") + 1);
        for (ProductsGroup group : ALL_GROUPS) {
            if (last.equals(group.getUrl())) {
                return group;
            }
        }
        return null;
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
}
