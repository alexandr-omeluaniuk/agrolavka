package ss.martin.security.jpa.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ss.entity.security.SystemUser;
import ss.entity.security.TenantEntity;
import ss.martin.core.constants.StandardRole;
import ss.martin.security.context.SecurityContext;

/**
 * Tenant entity listener.
 * @author ss
 */
public class TenantEntityListener {
    @PrePersist
    @PreUpdate
    protected void prePersistAndUpdate(TenantEntity entity) throws Exception {
        // exceptions for system user entity
        if (entity instanceof SystemUser) {
            StandardRole role = ((SystemUser) entity).getStandardRole();
            // case 1: allows ignore subscription during super admin creation process.
            if (StandardRole.ROLE_SUPER_ADMIN.equals(role)) {
                return;
            }
            // case 2: user try to finish registration
            Object auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof AnonymousAuthenticationToken) {
                return;
            }
            // case 3: super admin creates new subscription and subscription administrator
            if (StandardRole.ROLE_SUPER_ADMIN.equals(SecurityContext.currentUser().getStandardRole())) {
                return;
            }
        }
        // save current subscription for every tenant entity.
        if (entity != null) {
            TenantEntity tenantEntity = (TenantEntity) entity;
            tenantEntity.setSubscription(SecurityContext.subscription());
        }
    }
}
