package ss.agrolavka.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.*;
import ss.entity.security.EntityAudit_;
import ss.martin.core.dao.CoreDao;

import java.util.*;

/**
 * Product DAO implementation.
 * @author alex
 */
@Repository
class ProductDAOImpl implements ProductDAO {
    /** Entity manager. */
    @PersistenceContext
    private EntityManager em;
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Product> search(ProductsSearchRequest request) {
        if (request.getPage() == null || request.getPage() < 1) {
            request.setPage(1);
        }
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(Product.class);
        final var c = criteria.from(Product.class);
        c.fetch(Product_.group);
        final var predicates = createSearchCriteria(cb, c, request);
        criteria.select(c).where(predicates.toArray(Predicate[]::new));
        if (request.getOrder() != null && request.getOrderBy() != null) {
            if (EntityAudit_.CREATED_DATE.equals(request.getOrderBy())) {
                criteria.where(cb.greaterThan(c.get(Product_.quantity), 0d));
                if ("asc".equals(request.getOrder())) {
                    criteria.orderBy(cb.asc(c.get(EntityAudit_.createdDate)));
                } else {
                    criteria.orderBy(cb.desc(c.get(EntityAudit_.createdDate)));
                }
            } else {
                if ("asc".equals(request.getOrder())) {
                    criteria.orderBy(cb.asc(c.get(request.getOrderBy())));
                } else {
                    criteria.orderBy(cb.desc(c.get(request.getOrderBy())));
                }
            }
        } else {
            criteria.orderBy(cb.asc(c.get(Product_.name)));
        }
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
        criteriaCount.select(sum).where(predicates.toArray(Predicate[]::new));
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
    @Transactional(propagation = Propagation.SUPPORTS)
    private List<Predicate> createSearchCriteria(CriteriaBuilder cb, Root<Product> c,
            ProductsSearchRequest request) {
        List<Predicate> predicates = new ArrayList<>();
        if (!request.isIncludesHidden()) {
            predicates.add(cb.equal(c.get(Product_.hidden), false));
        }
        if (request.getGroupId() != null) {
            ProductsGroup targetGroup = coreDAO.findById(request.getGroupId(), ProductsGroup.class);
            Map<String, List<ProductsGroup>> groupsMap = new HashMap<>();
            List<ProductsGroup> allGroups = coreDAO.getAll(ProductsGroup.class);
            for (ProductsGroup group : allGroups) {
                if (group.getParentId() != null) {
                    if (!groupsMap.containsKey(group.getParentId())) {
                        groupsMap.put(group.getParentId(), new ArrayList<>());
                    }
                    groupsMap.get(group.getParentId()).add(group);
                }
            }
            Set<Long> groupIds = new HashSet<>();
            walkProductGroup(targetGroup, groupIds, groupsMap);
            Join<Product, ProductsGroup> productGroup = c.join(Product_.group, JoinType.LEFT);
            predicates.add(productGroup.get(ProductsGroup_.id).in(groupIds));
        }
        if (request.getText() != null && !request.getText().isBlank()) {
            predicates.add(cb.like(cb.upper(c.get(Product_.name)), "%" + request.getText().toUpperCase() + "%"));
        }
        if (request.getCode() != null && !request.getCode().isBlank()) {
            predicates.add(cb.like(cb.upper(c.get(Product_.code)), "%" + request.getCode().toUpperCase() + "%"));
        }
        if (request.isAvailable()) {
            predicates.add(cb.greaterThan(c.get(Product_.quantity), 0d));
        }
        if (request.isWithDiscounts()) {
            predicates.add(cb.isNotNull(c.get(Product_.discount)));
        }
        return predicates;
    }
    
    @Transactional(propagation = Propagation.SUPPORTS)
    private void walkProductGroup(ProductsGroup group, Set<Long> groupIds, Map<String, List<ProductsGroup>> groupsMap) {
        groupIds.add(group.getId());
        if (groupsMap.containsKey(group.getExternalId())) {
            groupsMap.get(group.getExternalId()).stream().forEach(g -> {
                walkProductGroup(g, groupIds, groupsMap);
            });
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteProductsByNotProductGroupIDs(Set<String> groupExternalIds) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> c = criteria.from(Product.class);
        Join<Product, ProductsGroup> productGroup = c.join(Product_.group, JoinType.LEFT);
        criteria.select(c).where(cb.not(productGroup.get(ProductsGroup_.externalId).in(groupExternalIds)));
        List<Product> entities = em.createQuery(criteria).getResultList();
        for (Product product : entities) {
            em.remove(product);
        }
        em.flush();
    }
    
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Product getProductByUrl(String url) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> c = criteria.from(Product.class);
        criteria.select(c).where(cb.equal(c.get(Product_.url), url));
        List<Product> list = em.createQuery(criteria).getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void resetDiscounts(List<Discount> discounts) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Product> criteria = cb.createCriteriaUpdate(Product.class);
        Root<Product> c = criteria.from(Product.class);
        Expression<Discount> expr = cb.nullLiteral(Discount.class);
        criteria.set(c.get(Product_.discount), expr);
        criteria.where(c.get(Product_.discount).in(discounts));
        em.createQuery(criteria).executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public List<Product> getLastModifiedProducts(final Date minDate) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> c = criteria.from(Product.class);
        criteria.select(c).where(cb.greaterThanOrEqualTo(c.get(Product_.UPDATED), minDate));
        return em.createQuery(criteria).getResultList();
    }

    @Override
    public List<Product> getByExternalIds(List<String> externalIds) {
        if (externalIds.isEmpty()) {
            return Collections.emptyList();
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> c = criteria.from(Product.class);
        criteria.select(c).where(c.get(Product_.EXTERNAL_ID).in(externalIds));
        return em.createQuery(criteria).getResultList();
    }
}
