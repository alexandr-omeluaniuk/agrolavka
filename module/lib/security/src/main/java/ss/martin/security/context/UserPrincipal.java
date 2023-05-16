package ss.martin.security.context;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ss.entity.security.SystemUser;
import ss.entity.security.UserAgent;

/**
 * SystemUser principal.
 * @author Alexandr Omeluaniuk
 */
public class UserPrincipal extends UsernamePasswordAuthenticationToken {
    /** SystemUser. */
    private SystemUser user;
    /** User agent of logged in user. */
    private UserAgent userAgent;
    
    /**
     * Constructor.
     * @param username - username.
     * @param password - password.
     * @param authorities - authorities.
     */
    public UserPrincipal(String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SystemUser getUser() {
        return user;
    }
    
    public void setUser(SystemUser user) {
        this.user = user;
    }
    
    public UserAgent getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }
}
