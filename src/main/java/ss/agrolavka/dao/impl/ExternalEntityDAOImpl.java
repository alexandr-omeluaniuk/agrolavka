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
import ss.agrolavka.dao.ExternalEntityDAO;
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
        criteria.where(cb.not(c.get(ExternalEntity_.externalId).in(ids)));
        em.createQuery(criteria).executeUpdate();
    }
}
