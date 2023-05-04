package ss.entity.martin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import ss.martin.platform.util.TenantEntityListener;

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
    /**
     * @return the subscription
     */
    public Subscription getSubscription() {
        return subscription;
    }
    /**
     * @param subscription the subscription to set
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
