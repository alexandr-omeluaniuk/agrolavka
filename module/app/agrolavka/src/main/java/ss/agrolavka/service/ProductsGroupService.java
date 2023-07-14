package ss.agrolavka.service;

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
}
