/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.dao.OrderDAO;
import ss.agrolavka.wrapper.OrderSearchRequest;
import ss.entity.agrolavka.Address;
import ss.entity.agrolavka.Address_;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.Order_;

/**
 * Order DAO implementation.
 * @author alex
 */
@Repository
class OrderDAOImpl implements OrderDAO {
    /** Entity manager. */
    @PersistenceContext
    private EntityManager em;
    @Override
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
    @Override
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
        if (request.getStatus()!= null && !request.getStatus().isBlank()) {
            predicates.add(cb.equal(c.get(Order_.status), OrderStatus.valueOf(request.getStatus())));
        }
        return predicates;
    }
}
