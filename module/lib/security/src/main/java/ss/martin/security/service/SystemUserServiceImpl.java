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
    private CoreDao coreDAO;
    /** User DAO. */
    @Autowired
    private UserDao userDAO;
    /** Email service. */
    @Autowired
    private EmailService emailService;
    /** Password encoder. */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /** Platform configuration. */
    @Autowired
    private SuperUserConfiguration superUserConfig;
    /** Domain configuration. */
    @Autowired
    private DomainConfiguration domainConfig;
    /** Navigation configuration. */
    @Autowired
    private NavigationConfiguration navigationConfig;
// =================================================== PUBLIC =========================================================
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void startRegistration(SystemUser systemUser) throws Exception {
        if (userDAO.findByUsername(systemUser.getEmail()) != null) {
            throw new RegistrationUserException(RegistrationUserException.CODE_DUPLICATE_USER);
        }
        String validationString = UUID.randomUUID().toString();
        systemUser.setStatus(SystemUserStatus.REGISTRATION);
        systemUser.setValidationString(validationString);
        if (SecurityContext.currentUser().getStandardRole() == StandardRole.ROLE_SUPER_ADMIN) {
            em.persist(systemUser);
        } else {
            coreDAO.create(systemUser);
        }
        final var firstname = Optional.ofNullable(systemUser.getFirstname()).orElse("");
        EmailRequest emailRequest = new EmailRequest(
                new EmailContact(domainConfig.emailName(), domainConfig.email()),
                new EmailContact[] {
                    new EmailContact(firstname + " " + systemUser.getLastname(), systemUser.getEmail())
                },
                "New user registration",
                "Follow the link: " + domainConfig.host() + navigationConfig.registrationVerification() + "/" + validationString,
                new EmailAttachment[0]
        );
        emailService.sendEmail(emailRequest);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void finishRegistration(String validationString, String password) throws Exception {
        SystemUser systemUser = userDAO.getUserByValidationString(validationString);
        systemUser.setValidationString(null);
        systemUser.setStatus(SystemUserStatus.ACTIVE);
        systemUser.setPassword(bCryptPasswordEncoder.encode(password));
        em.merge(systemUser);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void superUserCheck() {
        SystemUser superAdmin = userDAO.getSuperUser();
        if (superAdmin == null && superUserConfig.email() != null
                && superUserConfig.lastname() != null && superUserConfig.password() != null) {
            LOG.info("super user is not exist, create it...");
            try {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                Subscription superAdminSubscription = new Subscription();
                superAdminSubscription.setActive(true);
                superAdminSubscription.setStarted(calendar.getTime());
                calendar.add(Calendar.YEAR, 33);
                superAdminSubscription.setExpirationDate(calendar.getTime());
                superAdminSubscription.setOrganizationName("Super Admin subscription");
                superAdminSubscription.setSubscriptionAdminEmail(superUserConfig.email());
                em.persist(superAdminSubscription);
                superAdmin = new SystemUser();
                superAdmin.setActive(true);
                superAdmin.setSubscription(superAdminSubscription);
                superAdmin.setEmail(superUserConfig.email());
                superAdmin.setFirstname(superUserConfig.firstname());
                superAdmin.setLastname(superUserConfig.lastname());
                superAdmin.setPassword(bCryptPasswordEncoder.encode(superUserConfig.password()));
                superAdmin.setStandardRole(StandardRole.ROLE_SUPER_ADMIN);
                superAdmin.setStatus(SystemUserStatus.ACTIVE);
                em.persist(superAdmin);
            } catch (Exception e) {
                LOG.warn("Unexpected error occurred during super user creation.", e);
            }
        }
    }
    @Override
    public SystemUser createSystemUser(SystemUser user) throws Exception {
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_USER);
        user.setSubscription(SecurityContext.currentUser().getSubscription());
        startRegistration(user);
        return user;
    }
}
