/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao.impl;

import java.util.List;
import java.util.Set;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.ExternalEntityDAO;
import ss.agrolavka.service.GroupProductsService;
import ss.entity.agrolavka.ExternalEntity;
import ss.entity.agrolavka.ExternalEntity_;

/**
 * External entity DAO implementation.
 * @author alex
 */
@Repository
class ExternalEntityDAOImpl implements ExternalEntityDAO {
    /** Entity manager. */
    @PersistenceContext
    private EntityManager em;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T extends ExternalEntity> List<T> getExternalEntitiesByIds(Set<String> ids, Class<T> cl) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = cb.createQuery(cl);
        Root<T> c = criteria.from(cl);
        criteria.select(c).where(c.get(ExternalEntity_.externalId).in(ids));
        return em.createQuery(criteria).getResultList();
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends ExternalEntity> void removeExternalEntitiesNotInIDs(Set<String> ids, Class<T> cl) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<T> criteria = cb.createCriteriaDelete(cl);
        Root<T> c = criteria.from(cl);
        criteria.where(cb.and(
                cb.not(c.get(ExternalEntity_.externalId).in(ids)),
                cb.notEqual(c.get(ExternalEntity_.EXTERNAL_ID), GroupProductsService.GROUPED_PRODUCT_EXTERNAL_ID)
        ));
        em.createQuery(criteria).executeUpdate();
    }
}
