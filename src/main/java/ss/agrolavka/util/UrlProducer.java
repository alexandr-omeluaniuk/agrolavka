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

/**
 * URL producer.
 * @author alex
 */
public class UrlProducer {
    /** Catalog. Key is group external ID, value is group children. */
    private static final Map<String, List<ProductsGroup>> CATALOG = new HashMap<>();
    /** Group parents map. Key is group external ID, value is parent group. */
    private static final Map<String, ProductsGroup> GROUPS_PARENT_MAP = new HashMap<>();
    
    public static synchronized void updateCatalog(List<ProductsGroup> groups) {
        CATALOG.clear();
        GROUPS_PARENT_MAP.clear();
        //List<ProductsGroup> roots = new ArrayList<>();
        Map<String, ProductsGroup> externalIdsMap = new HashMap<>();
        for (ProductsGroup group : groups) {
            externalIdsMap.put(group.getExternalId(), group);
            if (group.getParentId() != null) {
                if (!CATALOG.containsKey(group.getParentId())) {
                    CATALOG.put(group.getParentId(), new ArrayList<>());
                }
                CATALOG.get(group.getParentId()).add(group);
            }/* else {
                roots.add(group);
            }*/
        }
        for (ProductsGroup group : groups) {
            String parentId = group.getParentId();
            GROUPS_PARENT_MAP.put(group.getExternalId(), parentId == null ? null : externalIdsMap.get(parentId));
        }
    }
    
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
}
