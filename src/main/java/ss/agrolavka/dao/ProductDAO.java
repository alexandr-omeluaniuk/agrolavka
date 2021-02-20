/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import java.util.List;
import java.util.Set;
import ss.agrolavka.model.ProductsGroup;

/**
 * Product DAO.
 * @author alex
 */
public interface ProductDAO {
    /**
     * Get product groups by external IDs.
     * @param ids external IDs.
     * @return list of product groups.
     */
    List<ProductsGroup> getProductGroupsByExternalIds(Set<String> ids);
    /**
     * Remove product groups by external IDs.
     * @param ids external IDs.
     */
    void removeProductGroupsByExternalIDs(Set<String> ids);
}
