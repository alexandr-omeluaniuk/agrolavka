package ss.martin.security.model;

import ss.entity.martin.Subscription;
import ss.martin.core.constants.StandardRole;

/**
 * User permissions for UI.
 * @author ss
 */
public record UserPermissions(
    Subscription subscription,
    String fullname,
    Long userId,
    StandardRole standardRole
) {

    public UserPermissions() {
        this(null, null, null, null);
    }
}
