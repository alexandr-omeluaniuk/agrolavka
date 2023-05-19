package ss.entity.security;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * User agent.
 * @author alex
 */
@Entity
@Table(name = "user_agents")
public class UserAgent extends EntityAudit {
    /** User agent string. */
    @NotNull
    @Size(max = 1000)
    @Column(name = "user_agent_string", nullable = false, length = 1000)
    private String userAgentString;
    /** Firebase token. */
    @Size(max = 255)
    @Column(name = "firebase_token", length = 255)
    private String firebaseToken;
    /** Notification subscriptions. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userAgent", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<NotificationTopicSubscription> notificationSubscriptions;
    
    public String getUserAgentString() {
        return userAgentString;
    }
    
    public void setUserAgentString(String userAgentString) {
        this.userAgentString = userAgentString;
    }
    
    public String getFirebaseToken() {
        return firebaseToken;
    }
    
    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
    
    public List<NotificationTopicSubscription> getNotificationSubscriptions() {
        return notificationSubscriptions;
    }
    
    public void setNotificationSubscriptions(List<NotificationTopicSubscription> notificationSubscriptions) {
        this.notificationSubscriptions = notificationSubscriptions;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserAgent)) {
            return false;
        }
        UserAgent other = (UserAgent) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.martin.UserAgent[ id=" + getId() + " ]";
    }
}