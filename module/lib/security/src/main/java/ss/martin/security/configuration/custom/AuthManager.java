package ss.martin.security.configuration.custom;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.dao.UserDao;
import ss.martin.security.exception.SubscriptionHasExpiredException;

/**
 * Authentication provider.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthManager implements AuthenticationManager {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AuthManager.class);
    /** Password encoder. */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    /** SystemUser DAO. */
    @Autowired
    private UserDao userDao;
    
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = String.valueOf(auth.getPrincipal());
        String password = String.valueOf(auth.getCredentials());
        final var user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (SystemUserStatus.ACTIVE != user.getStatus()) {
            throw new DisabledException("User is deactivated: " + username);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password: " + password);
        }
        if (user.getSubscription().getExpirationDate().before(new Date())) {
            throw new SubscriptionHasExpiredException(user.getSubscription());
        }
        LOG.info("successfull authentication for [" + user + "] completed...");
        return SecurityContext.createPrincipal(user);
    }
}
