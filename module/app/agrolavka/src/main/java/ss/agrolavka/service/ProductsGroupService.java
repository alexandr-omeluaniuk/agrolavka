package ss.agrolavka.service;

import java.util.List;
import java.util.Map;
import ss.entity.agrolavka.ProductsGroup;

/**
 * Products group service.
 * @author alex
 */
public interface ProductsGroupService {
    
    /**
     * Create products group.
     * @param group group.
     * @return new group.
     */
    ProductsGroup create(ProductsGroup group);
    
    /**
     * Update products group.
     * @param group group.
     * @return updated group.
     */
    ProductsGroup update(ProductsGroup group);
    
    /**
     * Delete products group.
     * @param id group ID.
     */
    void delete(Long id);
    
    /**
     * Get top categories.
     * @return top product groups.
     */
    List<ProductsGroup> getTopCategories();
    
    /**
     * Get all groups.
     * @return all groups.
     */
    List<ProductsGroup> getAllGroups();
    
    /**
     * Get root groups.
     * @return root groups.
     */
    List<ProductsGroup> getRootProductGroups();
    
    /**
     * Get breadcrumb path for target product group.
     * @param group group.
     * @return sequence of product groups.
     */
    List<ProductsGroup> getBreadcrumbPath(ProductsGroup group);
    
    /**
     * Get categories tree. Key is products group external ID, value is child products groups.
     * @return map.
     */
    Map<String, List<ProductsGroup>> getCategoriesTree();
}
