package ss.martin.security.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.base.lang.ThrowingRunnable;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.api.RegistrationUserService;
import ss.martin.security.configuration.external.DomainConfiguration;
import ss.martin.security.configuration.external.NavigationConfiguration;
import ss.martin.security.configuration.external.SuperUserConfiguration;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.dao.UserDao;
import ss.martin.security.exception.RegistrationUserException;

import java.util.*;

/**
 * Registration user service implementation.
 * @author ss
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class RegistrationUserServiceImpl implements RegistrationUserService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationUserServiceImpl.class);
    /** DataModel manager. */
    @PersistenceContext
    private EntityManager em;
    /** Core DAO. */
    @Autowired
    private CoreDao coreDao;
    /** User DAO. */
    @Autowired
    private UserDao userDao;
    /** Password encoder. */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    /** Platform configuration. */
    @Autowired
    private SuperUserConfiguration superUserConfiguration;
    /** Domain configuration. */
    @Autowired
    private DomainConfiguration domainConfiguration;
    /** Navigation configuration. */
    @Autowired
    private NavigationConfiguration navigationConfiguration;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createSuperAdmin() {
        final var superAdmin = userDao.findSuperUser();
        if (superAdmin.isEmpty()) {
            LOG.info("super user is not exist, create it...");
            ((ThrowingRunnable) () -> {
                final var firstname = "System";
                final var lastname = "Administrator";
                final var email = Optional.ofNullable(superUserConfiguration.email()).orElse("system@admin.user");
                final var generatedPassword = UUID.randomUUID().toString();
                final var calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                final var superAdminSubscription = new Subscription();
                superAdminSubscription.setActive(true);
                superAdminSubscription.setStarted(calendar.getTime());
                calendar.add(Calendar.YEAR, 33);
                superAdminSubscription.setExpirationDate(calendar.getTime());
                superAdminSubscription.setOrganizationName("Super Admin subscription");
                superAdminSubscription.setSubscriptionAdminEmail(email);
                final var createdSubscription = coreDao.create(superAdminSubscription);
                final var admin = new SystemUser();
                admin.setActive(true);
                admin.setSubscription(superAdminSubscription);
                admin.setEmail(email);
                admin.setFirstname(firstname);
                admin.setLastname(lastname);
                admin.setPassword(passwordEncoder.encode(generatedPassword));
                admin.setStandardRole(StandardRole.ROLE_SUPER_ADMIN);
                admin.setStatus(SystemUserStatus.ACTIVE);
                admin.setSubscription(createdSubscription);
                SecurityContext.executeBehalfUser(admin, () -> coreDao.create(admin));
                LOG.info("Super admin generated password: " + generatedPassword);
            }).run();
        }
    }
    
    @Override
    @Secured("ROLE_SUBSCRIPTION_ADMINISTRATOR")
    public SystemUser createSubscriptionUser(final SystemUser user) {
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_USER);
        user.setSubscription(SecurityContext.currentUser().getSubscription());
        startRegistration(user);
        return user;
    }

    @Override
    @Secured("ROLE_SUPER_ADMIN")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Subscription createSubscriptionAndAdmin(Subscription subscription) {
        final var subscriptionAdmin = new SystemUser();
        subscriptionAdmin.setActive(true);
        subscriptionAdmin.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR);
        subscriptionAdmin.setLastname(subscription.getOrganizationName());
        subscriptionAdmin.setEmail(subscription.getSubscriptionAdminEmail());
        subscription = coreDao.create(subscription);
        LOG.debug("new subscription created: " + subscription);
        subscriptionAdmin.setSubscription(subscription);
        startRegistration(subscriptionAdmin);
        LOG.debug("new subscription administrator created: " + subscriptionAdmin);
        return subscription;
    }
    
    @Override
    public void finishRegistration(String validationString, String password) {
        userDao.findByValidationString(validationString).ifPresent(user -> {
            user.setValidationString(null);
            user.setStatus(SystemUserStatus.ACTIVE);
            user.setPassword(passwordEncoder.encode(password));
            SecurityContext.executeBehalfUser(user, () -> coreDao.update(user));
        });
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    private void startRegistration(final SystemUser systemUser) {
        if (userDao.findByUsername(systemUser.getEmail()).isPresent()) {
            throw new RegistrationUserException(RegistrationUserException.CODE_DUPLICATE_USER);
        }
        final var validationString = UUID.randomUUID().toString();
        systemUser.setStatus(SystemUserStatus.REGISTRATION);
        systemUser.setValidationString(validationString);
        SecurityContext.executeBehalfUser(systemUser, () -> coreDao.create(systemUser));
        final var link = domainConfiguration.host() + navigationConfiguration.registrationVerification()
                + "/" + validationString;
        System.out.println("Validation link: " + link);
    }
}
