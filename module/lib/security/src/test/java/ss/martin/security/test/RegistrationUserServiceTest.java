package ss.martin.security.test;

import ss.martin.security.test.util.DataFactory;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import ss.entity.security.SystemUser;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.notification.email.api.EmailService;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.dao.UserDao;
import ss.martin.security.exception.RegistrationUserException;
import ss.martin.test.AbstractComponentTest;
import ss.martin.security.api.RegistrationUserService;

public class RegistrationUserServiceTest extends AbstractComponentTest {
    
    @Autowired
    private RegistrationUserService systemUserService;
    
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
        final var superAdminsCount = coreDao.getAll(SystemUser.class).stream()
                .filter(u -> StandardRole.ROLE_SUPER_ADMIN.equals(u.getStandardRole()))
                .count();
        assertTrue(superAdminsCount > 0);
    }
    
    @Test
    public void testUsersRegistration() {
        final var superAdmin = userDao.findSuperUser().get();
        SecurityContextHolder.getContext().setAuthentication(SecurityContext.createPrincipal(superAdmin));
        final var subscriptionAdminEmail = "subscription.admin@org.test";
        final var subscription = DataFactory.generateSubscription(subscriptionAdminEmail);
        
        // create subscription admin user
        final var created = systemUserService.createSubscriptionAndAdmin(subscription);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        final var user = userDao.findByUsername(subscriptionAdminEmail).get();
        assertEquals(SystemUserStatus.REGISTRATION, user.getStatus());
        assertEquals(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR, user.getStandardRole());
        assertNotNull(user.getValidationString());
        final var userWithSubscription = userDao.findByValidationString(user.getValidationString()).get();
        assertEquals(subscriptionAdminEmail, userWithSubscription.getSubscription().getSubscriptionAdminEmail());
        
        // duplicate user
        final var ex = assertThrows(
                RegistrationUserException.class, 
                () -> systemUserService.createSubscriptionAndAdmin(
                        DataFactory.generateSubscription(subscriptionAdminEmail)
                )
        );
        
        assertNotNull(ex);
        
        // finish registration behalf of anonymous user
        SecurityContextHolder.getContext().setAuthentication(null);
        final var password = "15RtyyySSSS786";
        systemUserService.finishRegistration(user.getValidationString(), password);
        
        final var user2 = userDao.findByUsername(subscriptionAdminEmail).get();
        assertEquals(SystemUserStatus.ACTIVE, user2.getStatus());
        assertEquals(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR, user2.getStandardRole());
        assertNull(user2.getValidationString());
        final var user2WithSubscription = userDao.findByUsername(user2.getEmail()).get();
        assertEquals(subscriptionAdminEmail, user2WithSubscription.getSubscription().getSubscriptionAdminEmail());
        
        // create subscription user
        SecurityContextHolder.getContext().setAuthentication(SecurityContext.createPrincipal(user));
        final var subscriptionUserEmail = "simple.user@subscription.test";
        final var subscriptionUser = DataFactory.generateSystemUser(subscriptionUserEmail, "Bob Murrey");
        
        final var createdSubscriptionUser = systemUserService.createSubscriptionUser(subscriptionUser);
        
        assertNotNull(createdSubscriptionUser);
    }
}
