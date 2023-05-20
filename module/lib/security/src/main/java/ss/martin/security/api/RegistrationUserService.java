package ss.martin.security.api;

import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;

/**
 * Registration user service.
 * @author ss
 */
public interface RegistrationUserService {
    
    /**
     * Create super admin user if it is not exist.
     */
    void createSuperAdmin();
    
    /**
     * Create new subscription and it's administrator.
     * @param subscription subscription.
     * @return created subscription.
     */
    Subscription createSubscriptionAndAdmin(Subscription subscription);
    
    /**
     * Create system user with ROLE_SUBSCRIPTION_USER.
     * @param user user without ID.
     * @return new system user.
     */
    SystemUser createSubscriptionUser(SystemUser user);
    
    /**
     * Finish user registration.
     * @param validationString validation string.
     * @param password password.
     */
    void finishRegistration(String validationString, String password);
}
