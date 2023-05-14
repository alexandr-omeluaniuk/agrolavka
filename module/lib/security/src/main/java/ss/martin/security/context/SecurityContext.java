package ss.martin.security.context;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.base.lang.ThrowingRunnable;

/**
 * Security context.
 * Provided access to current security context.
 * @author ss
 */
public class SecurityContext {
    /**
     * Get current user.
     * @return current user.
     */
    public static SystemUser currentUser() {
        UserPrincipal userPrincipal = principal();
        return userPrincipal == null ? null : userPrincipal.getUser();
    }
    /**
     * Get user principal.
     * @return user principal.
     */
    public static UserPrincipal principal() {
        Object auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UserPrincipal userPrincipal) {
            return userPrincipal;
        } else {
            return null;
        }
    }
    /**
     * Get current user subscription.
     * @return subscription.
     */
    public static Subscription subscription() {
        return currentUser().getSubscription();
    }
    /**
     * Create principal for user.
     * @param user user.
     * @return principal.
     */
    public static UserPrincipal createPrincipal(SystemUser user) {
        GrantedAuthority ga = new SimpleGrantedAuthority(user.getStandardRole().name());
        List<GrantedAuthority> gaList = new ArrayList<>();
        gaList.add(ga);
        UserPrincipal principal = new UserPrincipal(user.getEmail(), user.getPassword(), gaList);
        principal.setUser(user);
        return principal;
    }
    
    public static void executeBehalfUser(final SystemUser user, final ThrowingRunnable runnable) {
        final var currentPrincipal = principal();
        try {
            SecurityContextHolder.getContext().setAuthentication(createPrincipal(user));
            runnable.run();
        } finally {
            SecurityContextHolder.getContext().setAuthentication(currentPrincipal);
        }
    }
}
