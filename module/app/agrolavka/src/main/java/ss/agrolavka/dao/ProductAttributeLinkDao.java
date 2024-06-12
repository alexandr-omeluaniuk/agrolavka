package ss.agrolavka.dao;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.entity.agrolavka.ProductAttributeLink;
import ss.entity.agrolavka.ProductAttributeLink_;

import java.util.List;

@Repository
public class ProductAttributeLinkDao {

    @Autowired
    private EntityManager em;

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteProductLinks(Long productId) {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createCriteriaDelete(ProductAttributeLink.class);
        final var c = criteria.from(ProductAttributeLink.class);
        criteria.where(cb.and(
            cb.equal(c.get(ProductAttributeLink_.PRODUCT_ID), productId)
        ));
        em.createQuery(criteria).executeUpdate();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ProductAttributeLink> getProductLinks(Long productId) {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(ProductAttributeLink.class);
        final var c = criteria.from(ProductAttributeLink.class);
        criteria.where(cb.and(
            cb.equal(c.get(ProductAttributeLink_.PRODUCT_ID), productId)
        ));
        return em.createQuery(criteria).getResultList();
    }
}
