package ss.martin.security.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.entity.security.SystemUser;
import ss.entity.security.SystemUser_;
import ss.martin.core.constants.StandardRole;

/**
 * SystemUser DAO implementation.
 * @author Alexandr Omeluaniuk
 */
@Repository
public class UserDao {
    /** Entity manager. */
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Find user by username.
     * @param username username/email.
     * @return user or null.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<SystemUser> findByUsername(final String username) {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(SystemUser.class);
        final var c = criteria.from(SystemUser.class);
        c.fetch(SystemUser_.subscription);
        criteria.select(c).where(cb.equal(c.get(SystemUser_.email), username));
        return em.createQuery(criteria).getResultStream().findFirst();
    }
    
    /**
     * Find super user.
     * @return super user or null.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<SystemUser> findSuperUser() {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(SystemUser.class);
        final var c = criteria.from(SystemUser.class);
        criteria.select(c).where(cb.equal(c.get(SystemUser_.standardRole), StandardRole.ROLE_SUPER_ADMIN));
        return em.createQuery(criteria).getResultStream().findFirst();
    }
    
    /**
     * Find user by validation string.
     * @param validationString validation string.
     * @return user or null.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<SystemUser> findByValidationString(final String validationString) {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(SystemUser.class);
        final var c = criteria.from(SystemUser.class);
        c.fetch(SystemUser_.subscription);
        criteria.select(c).where(cb.equal(c.get(SystemUser_.validationString), validationString));
        return em.createQuery(criteria).getResultStream().findFirst();
    }
}
