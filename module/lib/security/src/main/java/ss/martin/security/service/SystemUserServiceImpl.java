package ss.martin.security.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.base.lang.ThrowingRunnable;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.notification.email.api.EmailService;
import ss.martin.notification.email.api.model.EmailAttachment;
import ss.martin.notification.email.api.model.EmailContact;
import ss.martin.notification.email.api.model.EmailRequest;
import ss.martin.security.api.SystemUserService;
import ss.martin.security.configuration.external.DomainConfiguration;
import ss.martin.security.configuration.external.NavigationConfiguration;
import ss.martin.security.configuration.external.SuperUserConfiguration;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.dao.UserDao;
import ss.martin.security.exception.RegistrationUserException;

/**
 * System user service implementation.
 * @author ss
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class SystemUserServiceImpl implements SystemUserService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SystemUserService.class);
    /** DataModel manager. */
    @PersistenceContext
    private EntityManager em;
    /** Core DAO. */
    @Autowired
    private CoreDao coreDao;
    /** User DAO. */
    @Autowired
    private UserDao userDao;
    /** Email service. */
    @Autowired
    private EmailService emailService;
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
    public void startRegistration(SystemUser systemUser) {
        if (userDao.findByUsername(systemUser.getEmail()) != null) {
            throw new RegistrationUserException(RegistrationUserException.CODE_DUPLICATE_USER);
        }
        String validationString = UUID.randomUUID().toString();
        systemUser.setStatus(SystemUserStatus.REGISTRATION);
        systemUser.setValidationString(validationString);
        if (SecurityContext.currentUser().getStandardRole() == StandardRole.ROLE_SUPER_ADMIN) {
            em.persist(systemUser);
        } else {
            coreDao.create(systemUser);
        }
        final var firstname = Optional.ofNullable(systemUser.getFirstname()).orElse("");
        final var link = domainConfiguration.host() + navigationConfiguration.registrationVerification() 
                + "/" + validationString;
        EmailRequest emailRequest = new EmailRequest(
                new EmailContact(domainConfiguration.emailName(), domainConfiguration.email()),
                new EmailContact[] {
                    new EmailContact(firstname + " " + systemUser.getLastname(), systemUser.getEmail())
                },
                "New user registration",
                String.format("Follow the link: %s", link),
                new EmailAttachment[0]
        );
        emailService.sendEmail(emailRequest);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void finishRegistration(String validationString, String password) {
        SystemUser systemUser = userDao.getUserByValidationString(validationString);
        systemUser.setValidationString(null);
        systemUser.setStatus(SystemUserStatus.ACTIVE);
        systemUser.setPassword(passwordEncoder.encode(password));
        em.merge(systemUser);
    }
    
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
                em.persist(superAdminSubscription);
                final var admin = new SystemUser();
                admin.setActive(true);
                admin.setSubscription(superAdminSubscription);
                admin.setEmail(email);
                admin.setFirstname(firstname);
                admin.setLastname(lastname);
                admin.setPassword(passwordEncoder.encode(generatedPassword));
                admin.setStandardRole(StandardRole.ROLE_SUPER_ADMIN);
                admin.setStatus(SystemUserStatus.ACTIVE);
                em.persist(admin);
                LOG.info("Super admin generated password: " + generatedPassword);
            }).run();
        }
    }
    
    @Override
    public SystemUser createSubscriptionUser(final SystemUser user) {
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_USER);
        user.setSubscription(SecurityContext.currentUser().getSubscription());
        startRegistration(user);
        return user;
    }
}
