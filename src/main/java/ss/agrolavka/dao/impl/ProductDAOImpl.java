/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao.impl;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.model.ProductsGroup;
import ss.agrolavka.model.ProductsGroup_;

/**
 * Product DAO implementation.
 * @author alex
 */
@Repository
class ProductDAOImpl implements ProductDAO {
    /** DataModel manager. */
    @PersistenceContext
    private EntityManager em;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ProductsGroup> getProductGroupsByExternalIds(Set<String> ids) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductsGroup> criteria = cb.createQuery(ProductsGroup.class);
        Root<ProductsGroup> c = criteria.from(ProductsGroup.class);
        criteria.select(c).where(c.get(ProductsGroup_.externalId).in(ids));
        return em.createQuery(criteria).getResultList();
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeProductGroupsByExternalIDs(Set<String> ids) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<ProductsGroup> criteria = cb.createCriteriaDelete(ProductsGroup.class);
        Root<ProductsGroup> c = criteria.from(ProductsGroup.class);
        criteria.where(cb.not(c.get(ProductsGroup_.externalId).in(ids)));
        em.createQuery(criteria).executeUpdate();
    }
}
