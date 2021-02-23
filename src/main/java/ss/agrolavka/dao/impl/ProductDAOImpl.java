/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.model.Product;
import ss.agrolavka.model.Product_;
import ss.agrolavka.model.ProductsGroup;
import ss.agrolavka.model.ProductsGroup_;
import ss.agrolavka.wrapper.ProductsSearchRequest;

/**
 * Product DAO implementation.
 * @author alex
 */
@Repository
class ProductDAOImpl implements ProductDAO {
    /** Entity manager. */
    @PersistenceContext
    private EntityManager em;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Product> search(ProductsSearchRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> c = criteria.from(Product.class);
        List<Predicate> predicates = createSearchCriteria(cb, c, request);
        criteria.select(c).where(predicates.toArray(new Predicate[0]));
        return em.createQuery(criteria)
                .setFirstResult((request.getPage() - 1) * request.getPageSize())
                .setMaxResults(request.getPageSize()).getResultList();
    }
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long count(ProductsSearchRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaCount = cb.createQuery(Long.class);
        Root<Product> c = criteriaCount.from(Product.class);
        Expression<Long> sum = cb.count(c);
        List<Predicate> predicates = createSearchCriteria(cb, c, request);
        criteriaCount.select(sum).where(predicates.toArray(new Predicate[0]));
        List<Long> maxList = em.createQuery(criteriaCount).getResultList();
        return maxList.iterator().next();
    }
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Set<ProductsGroup> getCatalogProductGroups() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductsGroup> criteria = cb.createQuery(ProductsGroup.class);
        Root<ProductsGroup> c = criteria.from(ProductsGroup.class);
        c.fetch(ProductsGroup_.products, JoinType.LEFT);
//        Join<ProductsGroup, Product> product = c.join(ProductsGroup_.products, JoinType.LEFT);
//        criteria.select(c).groupBy(c.get(ProductsGroup_.id))
//                .having(cb.ge(cb.count(product), 1));
        return new HashSet<>(em.createQuery(criteria).getResultList());
    }
    /**
     * Create search criteria from search request.
     * @param cb criteria builder.
     * @param c root entity.
     * @param request search request.
     * @return list of criteria.
     */
    private List<Predicate> createSearchCriteria(CriteriaBuilder cb, Root<Product> c,
            ProductsSearchRequest request) {
        List<Predicate> predicates = new ArrayList<>();
        if (request.getGroupId() != null) {
            Join<Product, ProductsGroup> productGroup = c.join(Product_.group, JoinType.LEFT);
            predicates.add(cb.equal(productGroup.get(ProductsGroup_.id), request.getGroupId()));
        }
        if (request.getText() != null && !request.getText().isBlank()) {
            predicates.add(cb.like(cb.upper(c.get(Product_.name)), "%" + request.getText().toUpperCase() + "%"));
        }
        return predicates;
    }
}
