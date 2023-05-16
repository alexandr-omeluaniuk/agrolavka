/*
 * Copyright (C) 2018 Wisent Media
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ss.martin.security.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.entity.security.EntityAudit_;
import ss.entity.security.SystemUser;
import ss.entity.security.SystemUser_;
import ss.entity.security.UserAgent;
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
    
    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<SystemUser> findByUsername(final String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SystemUser> criteria = cb.createQuery(SystemUser.class);
        Root<SystemUser> c = criteria.from(SystemUser.class);
        c.fetch(SystemUser_.subscription);
        criteria.select(c).where(cb.equal(c.get(SystemUser_.email), username));
        return em.createQuery(criteria).getResultStream().findFirst();
    }
    
    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<SystemUser> findSuperUser() {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(SystemUser.class);
        final var c = criteria.from(SystemUser.class);
        criteria.select(c).where(cb.equal(c.get(SystemUser_.standardRole), StandardRole.ROLE_SUPER_ADMIN));
        return em.createQuery(criteria).getResultStream().findFirst();
    }
    
    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<SystemUser> getUserByValidationString(final String validationString) {
        final var cb = em.getCriteriaBuilder();
        final var criteria = cb.createQuery(SystemUser.class);
        final var c = criteria.from(SystemUser.class);
        c.fetch(SystemUser_.subscription, JoinType.LEFT);
        criteria.select(c).where(cb.equal(c.get(SystemUser_.validationString), validationString));
        return em.createQuery(criteria).getResultStream().findFirst();
    }
    
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserAgent> getUserAgents(SystemUser user) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserAgent> criteria = cb.createQuery(UserAgent.class);
        Root<UserAgent> c = criteria.from(UserAgent.class);
        criteria.select(c).where(cb.equal(c.get(EntityAudit_.createdBy), user));
        return em.createQuery(criteria).getResultList();
    }
}
