package ss.martin.security.test;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.notification.email.api.EmailService;
import ss.martin.security.api.SystemUserService;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.dao.UserDao;
import ss.martin.security.exception.RegistrationUserException;
import ss.martin.test.AbstractComponentTest;

public class SystemUserServiceTest extends AbstractComponentTest {
    
    @Autowired
    private SystemUserService systemUserService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CoreDao coreDao;
    
    @MockBean
    private EmailService emailService;
    
    @Test
    public void testCreateSuperAdmin() {
        assertNotNull(userDao.findSuperUser());
        systemUserService.createSuperAdmin();
        assertEquals(1, coreDao.getAll(SystemUser.class).size());
    }
    
    @Test
    public void testCreateSubscriptionAndUsers() {
        final var superAdmin = userDao.findSuperUser().get();
        SecurityContextHolder.getContext().setAuthentication(SecurityContext.createPrincipal(superAdmin));
        final var subscriptionAdminEmail = "subscription.admin@org.test";
        final var subscription = generateSubscription(subscriptionAdminEmail);
        // create subscription admin user
        final var created = systemUserService.createSubscriptionAndAdmin(subscription);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        final var user = userDao.findByUsername(subscriptionAdminEmail).get();
        assertEquals(SystemUserStatus.REGISTRATION, user.getStatus());
        assertEquals(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR, user.getStandardRole());
        assertNotNull(user.getValidationString());
        final var userWithSubscription = userDao.getUserByValidationString(user.getValidationString()).get();
        assertEquals(subscriptionAdminEmail, userWithSubscription.getSubscription().getSubscriptionAdminEmail());
        // duplicate user
        final var ex = assertThrows(
                RegistrationUserException.class, 
                () -> systemUserService.createSubscriptionAndAdmin(generateSubscription(subscriptionAdminEmail))
        );
        assertNotNull(ex);
        // finish registration behalf of anonymous user
        SecurityContextHolder.getContext().setAuthentication(null);
        final var password = "15RtyyySSSS786";
        systemUserService.finishRegistration(user.getValidationString(), password);
    }
    
    private Subscription generateSubscription(final String email) {
        final var subscription = new Subscription();
        subscription.setOrganizationName("Jack Daniels");
        subscription.setSubscriptionAdminEmail(email);
        subscription.setStarted(new Date());
        subscription.setExpirationDate(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365)));
        return subscription;
    }
}
