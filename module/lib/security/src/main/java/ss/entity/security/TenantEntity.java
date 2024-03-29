package ss.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import ss.entity.martin.DataModel;
import ss.entity.martin.Subscription;
import ss.martin.security.jpa.listener.TenantEntityListener;

/**
 * Tenant entity.
 * @author ss
 */
@MappedSuperclass
@EntityListeners(TenantEntityListener.class)
public abstract class TenantEntity extends DataModel {
    /** Subscription. */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;
    
    public Subscription getSubscription() {
        return subscription;
    }
    
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
