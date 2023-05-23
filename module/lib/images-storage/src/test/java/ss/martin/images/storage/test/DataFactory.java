package ss.martin.images.storage.test;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.security.core.context.SecurityContextHolder;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;

public class DataFactory {
    
    public static Subscription generateSubscription(final String email) {
        final var subscription = new Subscription();
        subscription.setOrganizationName("Jack Daniels");
        subscription.setSubscriptionAdminEmail(email);
        subscription.setStarted(new Date());
        subscription.setExpirationDate(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365)));
        return subscription;
    }
    
    public static SystemUser generateSystemUser(final String email, final String lastname) {
        final var user = new SystemUser();
        user.setEmail(email);
        user.setLastname(lastname);
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_USER);
        user.setStatus(SystemUserStatus.ACTIVE);
        return user;
    }
    
    public static UserPrincipal generatePrincipal() {
        final var email = "jwt.test@test.com";
        final var user = DataFactory.generateSystemUser(email, "Alan Gray");
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_USER);
        return SecurityContext.createPrincipal(user);
    }
    
    public static void silentAuthentication(final CoreDao coreDao) {
        final var email = "luna@cat.test";
        final var subscription = coreDao.create(DataFactory.generateSubscription(email));
        final var user = DataFactory.generateSystemUser(email, "Luna Cat");
        user.setSubscription(subscription);
        SecurityContext.executeBehalfUser(user, () -> {
            coreDao.create(user);
        });
        SecurityContextHolder.getContext().setAuthentication(SecurityContext.createPrincipal(user));
    }
}
