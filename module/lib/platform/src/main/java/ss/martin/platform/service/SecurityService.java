package ss.martin.platform.service;

import java.util.Set;
import ss.entity.martin.DataModel;
import ss.martin.platform.constants.EntityPermission;
import ss.martin.platform.wrapper.UserPermissions;

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
     */
    Set<EntityPermission> getEntityPermissions(Class<? extends DataModel> clazz);
    
    /**
     * Background authentication for user.
     * @param username username.
     * @param password password.
     */
    void backgroundAuthentication(String username, String password);
}
