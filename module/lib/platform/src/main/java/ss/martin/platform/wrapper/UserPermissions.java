package ss.martin.platform.wrapper;

import ss.entity.martin.Subscription;
import ss.entity.security.UserAgent;
import ss.martin.core.constants.StandardRole;

/**
 * User permissions for UI.
 * @author ss
 */
public class UserPermissions {
    /** Subscription. */
    private Subscription subscription;
    /** User full name. */
    private String fullname;
    /** User ID. */
    private Long userId;
    /** STandard role. */
    private StandardRole standardRole;
    /** Has Firebase token. */
    private UserAgent userAgent;
    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }
    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
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
    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    /**
     * @return the standardRole
     */
    public StandardRole getStandardRole() {
        return standardRole;
    }
    /**
     * @param standardRole the standardRole to set
     */
    public void setStandardRole(StandardRole standardRole) {
        this.standardRole = standardRole;
    }
    /**
     * @return the userAgent
     */
    public UserAgent getUserAgent() {
        return userAgent;
    }
    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }
}
