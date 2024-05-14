/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.wrapper.OrderSearchRequest;
import ss.entity.agrolavka.Address;
import ss.entity.agrolavka.Address_;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.Order_;

import java.util.ArrayList;
import java.util.List;

/**
 * Order DAO implementation.
 * @author alex
 */
@Repository
public class OrderDAO {
    /** Entity manager. */
    @PersistenceContext
    private EntityManager em;
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Order> search(OrderSearchRequest request) throws Exception {
        if (request.getPage() == null || request.getPage() < 1) {
            request.setPage(1);
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> c = criteria.from(Order.class);
        List<Predicate> predicates = createSearchCriteria(cb, c, request);
        criteria.select(c).where(predicates.toArray(new Predicate[0]));
        if (request.getOrder() != null && request.getOrderBy() != null) {
            if ("asc".equals(request.getOrder())) {
                criteria.orderBy(cb.asc(c.get(request.getOrderBy())));
            } else {
                criteria.orderBy(cb.desc(c.get(request.getOrderBy())));
            }
        } else {
            criteria.orderBy(cb.desc(c.get(Order_.created)));
        }
        return em.createQuery(criteria)
                .setFirstResult((request.getPage() - 1) * request.getPageSize())
                .setMaxResults(request.getPageSize()).getResultList();
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long count(OrderSearchRequest request) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaCount = cb.createQuery(Long.class);
        Root<Order> c = criteriaCount.from(Order.class);
        Expression<Long> sum = cb.count(c);
        List<Predicate> predicates = createSearchCriteria(cb, c, request);
        criteriaCount.select(sum).where(predicates.toArray(new Predicate[0]));
        List<Long> maxList = em.createQuery(criteriaCount).getResultList();
        return maxList.iterator().next();
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    private List<Predicate> createSearchCriteria(CriteriaBuilder cb, Root<Order> c,
            OrderSearchRequest request) throws Exception {
        List<Predicate> predicates = new ArrayList<>();
        Join<Order, Address> address = c.join(Order_.address, JoinType.LEFT);
        if (request.getText() != null && !request.getText().isBlank()) {
            predicates.add(cb.or(
                    cb.like(cb.upper(address.get(Address_.city)), "%" + request.getText().toUpperCase() + "%"),
                    cb.like(cb.upper(address.get(Address_.street)), "%" + request.getText().toUpperCase() + "%"),
                    cb.like(cb.upper(address.get(Address_.flat)), "%" + request.getText().toUpperCase() + "%"),
                    cb.like(cb.upper(address.get(Address_.house)), "%" + request.getText().toUpperCase() + "%"),
                    cb.like(cb.upper(address.get(Address_.postcode)), "%" + request.getText().toUpperCase() + "%"),
                    cb.like(cb.upper(c.get(Order_.phone)), "%" + request.getText().toUpperCase() + "%")
            ));
        }
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            predicates.add(cb.equal(c.get(Order_.status), OrderStatus.valueOf(request.getStatus())));
        } else {
            if (!request.isShowClosed()) {
                predicates.add(cb.notEqual(c.get(Order_.status), OrderStatus.CLOSED));
            } else {
                predicates.add(cb.equal(c.get(Order_.status), OrderStatus.CLOSED));
            }
        }
        return predicates;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Cacheable(value = CacheKey.PURCHASE_HISTORY)
    public List<Order> getPurchaseHistory(final String phoneNumber) {
        final Query query = em.createNativeQuery(
            "SELECT * FROM customer_order WHERE SUBSTRING(REGEXP_REPLACE(phone, '[^0-9]', ''), -7) = "
                + phoneNumber + " order by created desc limit 10",
            Order.class
        );
        return query.getResultList();
    }
}
