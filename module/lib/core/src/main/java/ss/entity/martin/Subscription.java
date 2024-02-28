package ss.entity.martin;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ss.martin.core.anno.EntityAccess;
import ss.martin.core.constants.StandardRole;

import java.util.Date;

/**
 * Subscription.
 * @author ss
 */
@Entity
@Table(name = "subscription")
@EntityAccess(roles = { StandardRole.ROLE_SUPER_ADMIN })
public class Subscription extends DataModel implements SoftDeleted {
    /** Organization name. */
    @NotEmpty
    @Size(max = 255)
    @Column(name = "organization_name", length = 255)
    private String organizationName;
    /** Started. */
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "started", nullable = false)
    private Date started;
    /** Expiration date. */
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;
    /** Subscription admin email. */
    @NotEmpty
    @Email
    @Size(max = 255)
    @Column(name = "admin_email", length = 255, nullable = false, updatable = false)
    private String subscriptionAdminEmail;
    /** Active. */
    @Column(name = "active", nullable = false)
    private boolean active;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSubscriptionAdminEmail() {
        return subscriptionAdminEmail;
    }

    public void setSubscriptionAdminEmail(String subscriptionAdminEmail) {
        this.subscriptionAdminEmail = subscriptionAdminEmail;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Subscription)) {
            return false;
        }
        Subscription other = (Subscription) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "Subscription[ id=" + getId() + " ]";
    }
}
