package ss.martin.security.api;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import ss.entity.martin.DataModel;
import ss.entity.security.UserAgent;
import ss.martin.security.constants.EntityPermission;
import ss.martin.security.model.UserPermissions;

/**
 * Security service.
 * @author Alexandr Omeluaniuk
 */
public interface SecurityService {
    /**
     * Get user permissions.
     * @return user permissions.
     * @throws Exception error.
     */
    UserPermissions getUserPermissions() throws Exception;
    /**
     * Get entity permissions.
     * @param clazz data model class.
     * @return set of permissions.
     * @throws Exception error.
     */
    Set<EntityPermission> getEntityPermissions(Class<? extends DataModel> clazz) throws Exception;
    /**
     * Background authentication for user.
     * @param username username.
     * @param password password.
     */
    void backgroundAuthentication(String username, String password);
    /**
     * Get current user agent.
     * @param httpRequest HTTP request.
     * @return user agent.
     */
    UserAgent getUserAgent(HttpServletRequest httpRequest);
}
