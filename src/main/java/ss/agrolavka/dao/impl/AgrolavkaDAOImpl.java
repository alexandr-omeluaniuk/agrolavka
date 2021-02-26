/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.AgrolavkaDAO;

/**
 * Core DAO implementation.
 * @author Alexandr Omeluaniuk
 */
@Repository
class AgrolavkaDAOImpl implements AgrolavkaDAO {
    /** DataModel manager. */
    @PersistenceContext
    private EntityManager em;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> T create(final T entity) {
        em.persist(entity);
        return entity;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> T update(final T entity) {
        T updated = em.merge(entity);
        return updated;
    }
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T> T findById(final Serializable id, final Class<T> cl) throws Exception {
        return em.find(cl, id);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> void delete(final Serializable id, final Class<T> cl) throws Exception {
        T entity = findById(id, cl);
        if (entity != null) {
            em.remove(entity);
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> void deleteAll(Class<T> cl) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<T> criteria = cb.createCriteriaDelete(cl);
        Root<T> c = criteria.from(cl);
        em.createQuery(criteria).executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> void massCreate(List<T> list) throws Exception {
        for (T entity : list) {
            em.persist(entity);
        }
        em.flush();
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> void massUpdate(List<T> list) throws Exception {
        for (T entity : list) {
            em.merge(entity);
        }
        em.flush();
    }
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T> List<T> getAll(Class<T> cl) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = cb.createQuery(cl);
        Root<T> c = criteria.from(cl);
        return em.createQuery(criteria).getResultList();
    }
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T> Long count(Class<T> cl) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaCount = cb.createQuery(Long.class);
        Root<T> c = criteriaCount.from(cl);
        Expression<Long> sum = cb.count(c);
        criteriaCount.select(sum);
        List<Long> maxList = em.createQuery(criteriaCount).getResultList();
        return maxList.iterator().next();
    }
}
