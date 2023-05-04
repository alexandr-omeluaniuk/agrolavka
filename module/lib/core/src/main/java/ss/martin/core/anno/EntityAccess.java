package ss.martin.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ss.martin.core.constants.StandardRole;

/**
 * Basic access to entity.
 * Based on current user standard role.
 * @author ss
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EntityAccess {
    /**
     * One or more standard security roles.
     * @return security roles.
     */
    public StandardRole[] roles();
}
