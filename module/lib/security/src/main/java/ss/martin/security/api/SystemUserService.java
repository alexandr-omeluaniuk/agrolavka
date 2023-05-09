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
     * @throws Exception error.
     */
    void startRegistration(SystemUser systemUser) throws Exception;
    /**
     * Finish user registration.
     * @param validationString validation string.
     * @param password password.
     * @throws Exception error.
     */
    void finishRegistration(String validationString, String password) throws Exception;
    /**
     * Check if super user exists.
     * If no - create it.
     */
    void superUserCheck();
    /**
     * Create system user with ROLE_SUBSCRIPTION_USER.
     * @param user user without ID.
     * @return new system user.
     * @throws Exception error.
     */
    SystemUser createSystemUser(SystemUser user) throws Exception;
}
