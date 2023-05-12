package ss.martin.security.api;

import ss.entity.security.SystemUser;

/**
 * System user service.
 * @author ss
 */
public interface SystemUserService {
    /**
     * Start user registration.
     * @param systemUser system user.
     */
    void startRegistration(SystemUser systemUser);
    /**
     * Finish user registration.
     * @param validationString validation string.
     * @param password password.
     */
    void finishRegistration(String validationString, String password);
    /**
     * Check if super user exists.
     * If no - create it.
     */
    void superUserCheck();
    /**
     * Create system user with ROLE_SUBSCRIPTION_USER.
     * @param user user without ID.
     * @return new system user.
     */
    SystemUser createSubscriptionUser(SystemUser user);
}
