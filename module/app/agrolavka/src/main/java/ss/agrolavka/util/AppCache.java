package ss.agrolavka.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ss.entity.agrolavka.ProductsGroup;

/**
 * Application cache.
 * @author alex
 */
public class AppCache {
    /** Group parents map. Key is group external ID, value is parent group. */
    private static final Map<String, ProductsGroup> GROUPS_PARENT_MAP = new HashMap<>();
    /** All groups. */
    private static final List<ProductsGroup> ALL_GROUPS = new ArrayList<>();
    /**
     * Update catalog data.
     * @param groups product groups.
     */
    public static synchronized void flushCache(List<ProductsGroup> groups) {
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
