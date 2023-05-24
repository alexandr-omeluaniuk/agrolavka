package ss.martin.platform.wrapper;

import ss.entity.martin.Subscription;
import ss.entity.security.UserAgent;
import ss.martin.core.constants.StandardRole;

/**
 * User permissions for UI.
 * @author ss
 */
public record UserPermissions(
    Subscription subscription,
    String fullname,
    Long userId,
    StandardRole standardRole,
    UserAgent userAgent
) {

    public UserPermissions() {
        this(null, null, null, null, null);
    }
}
