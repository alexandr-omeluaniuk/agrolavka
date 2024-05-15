/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.service.ProductService;
import ss.agrolavka.wrapper.OrderSearchRequest;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.*;
import ss.martin.core.dao.CoreDao;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Order DAO implementation.
 * @author alex
 */
@Repository
public class OrderDAO {
    /** Entity manager. */
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private ProductService productService;

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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> c = criteria.from(Order.class);
        c.fetch(Order_.europostLocationSnapshot, JoinType.LEFT);
        c.fetch(Order_.address, JoinType.LEFT);
        c.fetch(Order_.positions, JoinType.LEFT);
        final var fun = cb.function(
            "REGEXP_REPLACE",
            String.class,
            c.get(Order_.phone),
            cb.literal("[^0-9]"),
            cb.literal("")
        );
        criteria.select(c).where(
            cb.like(fun, "%" + phoneNumber)
        ).orderBy(cb.desc(c.get(Order_.created)));
        final var purchaseHistory = em.createQuery(criteria).getResultList();
        if (!purchaseHistory.isEmpty()) {
            final var productIds = purchaseHistory.stream()
                .flatMap(mapper -> mapper.getPositions().stream().map(OrderPosition::getProductId))
                .collect(Collectors.toSet());
            final var productsMap = coreDao.findByIds(productIds, Product.class).stream().collect(
                Collectors.toMap(Product::getId, Function.identity())
            );
            productsMap.values().forEach(product -> product.setVariants(productService.getVariants(product)));
            purchaseHistory.forEach(order -> {
                order.getPositions().forEach(position -> position.setProduct(productsMap.get(position.getProductId())));
            });
        }
        return purchaseHistory.stream()
            .filter(order -> order.getPositions().stream().anyMatch(position -> position.getProduct() != null))
            .collect(Collectors.toList());
    }
}
