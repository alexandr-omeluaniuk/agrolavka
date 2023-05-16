package ss.martin.security.exception;

import org.springframework.security.core.AuthenticationException;
import ss.entity.martin.Subscription;

/**
 * Subscription has expired.
 * @author ss
 */
public class SubscriptionHasExpiredException extends AuthenticationException {
    /**
     * Constructor.
     * @param subscription subscription.
     */
    public SubscriptionHasExpiredException(Subscription subscription) {
        super("Subscription has expired! " + subscription.getId());
    }
}
