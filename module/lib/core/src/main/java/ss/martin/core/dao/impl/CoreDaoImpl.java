package ss.martin.core.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.entity.martin.DataModel;
import ss.entity.martin.DataModel_;
import ss.entity.martin.SoftDeleted;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;

@Repository
class CoreDaoImpl implements CoreDao {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel> T create(final T entity) {
        em.persist(entity);
        return entity;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel> T update(final T entity) {
        final var updated = em.merge(entity);
        return updated;
    }
    
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T extends DataModel> T findById(final Serializable id, final Class<T> cl) {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(cl);
        final var c = criteria.from(cl);
        final var predicates = new ArrayList<>();
        predicates.add(cb.equal(c.get(DataModel_.id), id));
        criteria.select(c).where(predicates.toArray(Predicate[]::new));
        final var result = em.createQuery(criteria).getResultList();
        return result.stream().findFirst().orElse(null);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel> void delete(final Serializable id, final Class<T> cl) {
        Optional.ofNullable(findById(id, cl)).ifPresent(entity -> em.remove(entity));
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel> void massDelete(final Set<Long> ids, final Class<T> cl) {
        if (!ids.isEmpty()) {
            final var cb = em.getCriteriaBuilder();
            final var criteria = cb.createCriteriaDelete(cl);
            final var c = criteria.from(cl);
            criteria.where(c.get(DataModel_.id).in(ids));
            em.createQuery(criteria).executeUpdate();
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel> void massDelete(final List<T> entities) {
        final var ids = entities.stream().map(T::getId).collect(Collectors.toSet());
        if (!ids.isEmpty()) {
            massDelete(ids, entities.iterator().next().getClass());
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel> void massCreate(final List<T> list) {
        list.forEach(entity -> em.persist(entity));
        em.flush();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel> void massUpdate(final List<T> list) {
        list.forEach(entity -> em.merge(entity));
        em.flush();
    }
    
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T extends DataModel> EntitySearchResponse<T> searchEntities(
            final EntitySearchRequest searchRequest,
            final Class<T> cl
    ) {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(cl);
        final var c = criteria.from(cl);
        final var predicates = createSearchCriteria(cb, c, cl, searchRequest);
        criteria.select(c).where(predicates.toArray(Predicate[]::new));
        Optional.ofNullable(searchRequest.orderBy()).ifPresent((orderBy) -> {
            if ("asc".equals(searchRequest.order())) {
                criteria.orderBy(cb.asc(c.get(orderBy)));
            } else {
                criteria.orderBy(cb.desc(c.get(orderBy)));
            }
        });
        final var entities = em.createQuery(criteria)
                .setFirstResult((searchRequest.page() - 1) * searchRequest.pageSize())
                .setMaxResults(searchRequest.pageSize()).getResultList();
        if (searchRequest.ignoreCount()) {
            return new EntitySearchResponse<>(entities);
        } else {
            final var criteriaCount = cb.createQuery(Long.class);
            final var cCount = criteriaCount.from(cl);
            final var sum = cb.count(cCount);
            final var predicatesCount = createSearchCriteria(cb, cCount, cl, searchRequest);
            criteriaCount.select(sum).where(predicatesCount.toArray(Predicate[]::new));
            final var maxList = em.createQuery(criteriaCount).getResultStream();
            return new EntitySearchResponse<>(maxList.findFirst().orElse(0L), entities);
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel & SoftDeleted> void deactivateEntities(final Set<Long> ids, final Class<T> cl) {
        if (!ids.isEmpty()) {
            final var cb = em.getCriteriaBuilder();
            final var criteria = cb.createCriteriaUpdate(cl);
            final var c = criteria.from(cl);
            criteria.set(c.get(SoftDeleted.ACTIVE_FIELD_NAME), false).where(
                c.get(DataModel_.id).in(ids)
            );
            em.createQuery(criteria).executeUpdate();
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T extends DataModel & SoftDeleted> void activateEntities(final Set<Long> ids, final Class<T> cl) {
        if (!ids.isEmpty()) {
            final var cb = em.getCriteriaBuilder();
            final var criteria = cb.createCriteriaUpdate(cl);
            final var c = criteria.from(cl);
            criteria.set(c.get(SoftDeleted.ACTIVE_FIELD_NAME), true).where(
                c.get(DataModel_.id).in(ids)
            );
            em.createQuery(criteria).executeUpdate();
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T extends DataModel> Long count(final Class<T> cl) {
        final var cb = em.getCriteriaBuilder();
        final var criteriaCount = cb.createQuery(Long.class);
        final var c = criteriaCount.from(cl);
        final var sum = cb.count(c);
        criteriaCount.select(sum).where(new Predicate[0]);
        final var maxList = em.createQuery(criteriaCount).getResultStream();
        return maxList.findFirst().orElse(0L);
    }
    
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T extends DataModel> List<T> getAll(final Class<T> cl) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = cb.createQuery(cl);
        Root<T> c = criteria.from(cl);
        criteria.select(c).where(new Predicate[0]);
        return em.createQuery(criteria).getResultList();
    }
    
    private <T extends DataModel> List<Predicate> createSearchCriteria(
            final CriteriaBuilder cb, 
            final Root<T> c, 
            final Class<T> clazz,
            final EntitySearchRequest searchRequest
    ) {
        List<Predicate> predicates = new ArrayList<>();
        if (!searchRequest.showDeactivated() && SoftDeleted.class.isAssignableFrom(clazz)) {
            predicates.add(cb.equal(c.get(SoftDeleted.ACTIVE_FIELD_NAME), true));
        }
        return predicates;
    }
}
