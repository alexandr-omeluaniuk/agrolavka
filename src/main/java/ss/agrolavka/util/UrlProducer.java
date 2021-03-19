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
    /**
     * Transliterate Russian text.
     * @param message russian text.
     * @return transliterated text.
     */
    public static String transliterate(String message) {
        message = message.replace(",", " ");
        char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у',
            'ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л',
            'М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Ь','Э','Ю','Я','a','b','c','d','e',
            'f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E',
            'F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u",
            "f","h","ts","ch","sh","sch", "","i", "","e","ju","ja","A","B","V","G","D","E","E","Zh","Z","I","Y","K",
            "L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch", "","I", "","E","Ju","Ja","a","b","c",
            "d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C",
            "D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString().replace(" ", "-").toLowerCase();
    }
}
