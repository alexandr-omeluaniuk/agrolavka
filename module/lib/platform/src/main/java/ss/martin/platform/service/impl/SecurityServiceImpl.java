package ss.martin.platform.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ss.entity.martin.DataModel;
import ss.entity.security.SystemUser;
import ss.entity.security.UserAgent;
import ss.martin.core.anno.EntityAccess;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.platform.service.SecurityService;
import ss.martin.security.constants.EntityPermission;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;
import ss.martin.security.dao.UserDao;
import ss.martin.security.model.UserPermissions;

/**
 * Security service implementation.
 * @author Alexandr Omeluaniuk
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class SecurityServiceImpl implements SecurityService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);
    /** Authentication manager. */
    @Autowired
    private AuthenticationManager authManager;
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    /** User DAO. */
    @Autowired
    private UserDao userDAO;
    
    @Override
    public UserPermissions getUserPermissions() throws Exception {
        UserPermissions permissions = new UserPermissions();
        UserPrincipal principal = SecurityContext.principal();
        if (principal != null) {
            SystemUser currentUser = principal.getUser();
            permissions.setUserId(currentUser.getId());
            permissions.setSubscription(SecurityContext.subscription());
            permissions.setFullname((currentUser.getFirstname() == null ? "" : currentUser.getFirstname() + " ")
                    + currentUser.getLastname());
            permissions.setStandardRole(currentUser.getStandardRole());
            UserAgent ua = coreDAO.findById(principal.getUserAgent().getId(), UserAgent.class);
            principal.setUserAgent(ua);
            permissions.setUserAgent(ua);
        }
        return permissions;
    }
    @Override
    public Set<EntityPermission> getEntityPermissions(Class<? extends DataModel> clazz) throws Exception {
        Set<EntityPermission> permissions = new HashSet<>();
        // first level of security
        Optional.ofNullable(clazz.getAnnotation(EntityAccess.class)).ifPresentOrElse((anno) -> {
            Set<StandardRole> entityRoles = new HashSet(Arrays.asList(anno.roles()));
            if (entityRoles.contains(SecurityContext.currentUser().getStandardRole())) {
                permissions.addAll(Arrays.asList(EntityPermission.values()));
            }
        }, () -> {
            permissions.addAll(Arrays.asList(EntityPermission.values()));
        });
        return permissions;
    }
    @Override
    public void backgroundAuthentication(String username, String password) {
        Authentication a = authManager.authenticate(new UserPrincipal(username, password, new ArrayList<>()));
        SecurityContextHolder.getContext().setAuthentication(a);
    }
    @Override
    public UserAgent getUserAgent(HttpServletRequest httpRequest) {
        UserPrincipal principal = SecurityContext.principal();
        String userAgentString = httpRequest.getHeader("User-Agent");
        List<UserAgent> userAgents = userDAO.getUserAgents(principal.getUser());
        UserAgent userAgent = userAgents.stream().filter(ua -> {
            return userAgentString.equals(ua.getUserAgentString());
        }).findFirst().map(ua -> ua).orElseGet(() -> {
            UserAgent ua = new UserAgent();
            ua.setUserAgentString(userAgentString);
            try {
                coreDAO.create(ua);
            } catch (Exception ex) {
                LOG.error("can't create new user agent.", ex);
            }
            return ua;
        });
        return userAgent;
    }
}
