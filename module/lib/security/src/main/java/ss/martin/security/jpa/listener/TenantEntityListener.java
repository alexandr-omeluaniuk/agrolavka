package ss.martin.security.jpa.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Optional;
import ss.entity.security.TenantEntity;
import ss.martin.security.context.SecurityContext;

/**
 * Tenant entity listener.
 * @author ss
 */
public class TenantEntityListener {
    
    @PrePersist
    @PreUpdate
    protected void prePersistAndUpdate(final TenantEntity entity) {
        Optional.ofNullable(entity).ifPresent(e -> e.setSubscription(SecurityContext.subscription()));
    }
}
