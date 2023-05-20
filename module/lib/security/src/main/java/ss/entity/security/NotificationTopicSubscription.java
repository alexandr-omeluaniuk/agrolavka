package ss.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ss.entity.martin.DataModel;

/**
 * Notification topic subscription.
 * @author alex
 */
@Entity
@Table(name = "notification_topic_subscriptions")
public class NotificationTopicSubscription extends DataModel {
    /** Topic. */
    @NotNull
    @Size(max = 255)
    @Column(name = "topic", nullable = false, length = 255)
    private String topic;
    /** User agent. */
    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_agent_id")
    private UserAgent userAgent;
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public UserAgent getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NotificationTopicSubscription)) {
            return false;
        }
        NotificationTopicSubscription other = (NotificationTopicSubscription) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.martin.NotificationTopicSubscription[ id=" + getId() + " ]";
    }
}
