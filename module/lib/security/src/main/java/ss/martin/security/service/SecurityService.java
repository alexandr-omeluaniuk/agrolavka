package ss.martin.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ss.entity.security.SystemUser;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;
import ss.martin.security.model.UserPermissions;

import java.util.ArrayList;

/**
 * Security service implementation.
 * @author Alexandr Omeluaniuk
 */
@Service
public class SecurityService {
    /** Authentication manager. */
    @Autowired
    private AuthenticationManager authManager;
    
    public UserPermissions getUserPermissions() {
        UserPrincipal principal = SecurityContext.principal();
        if (principal != null) {
            SystemUser currentUser = principal.getUser();
            return new UserPermissions(
                SecurityContext.subscription(),
                (currentUser.getFirstname() == null ? "" : currentUser.getFirstname() + " ")
                    + currentUser.getLastname(),
                currentUser.getId(),
                currentUser.getStandardRole()
            );
        }
        return new UserPermissions();
    }
    
    public void backgroundAuthentication(final String username, final String password) {
        final var auth = authManager.authenticate(new UserPrincipal(username, password, new ArrayList<>()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
