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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ss.entity.martin.DataModel;
import ss.entity.security.SystemUser;
import ss.entity.security.UserAgent;
import ss.martin.core.anno.EntityAccess;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.platform.service.SecurityService;
import ss.martin.platform.constants.EntityPermission;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;
import ss.martin.security.dao.UserDao;
import ss.martin.platform.wrapper.UserPermissions;

/**
 * Security service implementation.
 * @author Alexandr Omeluaniuk
 */
@Service
class SecurityServiceImpl implements SecurityService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);
    /** Authentication manager. */
    @Autowired
    private AuthenticationManager authManager;
    /** Core DAO. */
    @Autowired
    private CoreDao coreDao;
    /** User DAO. */
    @Autowired
    private UserDao userDao;
    
    @Override
    public UserPermissions getUserPermissions() {
        UserPrincipal principal = SecurityContext.principal();
        if (principal != null) {
            SystemUser currentUser = principal.getUser();
            UserAgent ua = coreDao.findById(principal.getUserAgent().getId(), UserAgent.class);
            principal.setUserAgent(ua);
            return new UserPermissions(
                SecurityContext.subscription(),
                (currentUser.getFirstname() == null ? "" : currentUser.getFirstname() + " ")
                    + currentUser.getLastname(),
                currentUser.getId(),
                currentUser.getStandardRole(),
                ua
            );
        }
        return new UserPermissions();
    }
    
    @Override
    public Set<EntityPermission> getEntityPermissions(final Class<? extends DataModel> clazz) {
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
    public void backgroundAuthentication(final String username, final String password) {
        final var auth = authManager.authenticate(new UserPrincipal(username, password, new ArrayList<>()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    @Override
    public UserAgent getUserAgent(final HttpServletRequest httpRequest) {
        UserPrincipal principal = SecurityContext.principal();
        String userAgentString = httpRequest.getHeader("User-Agent");
        List<UserAgent> userAgents = userDao.getUserAgents(principal.getUser());
        UserAgent userAgent = userAgents.stream().filter(ua -> {
            return userAgentString.equals(ua.getUserAgentString());
        }).findFirst().map(ua -> ua).orElseGet(() -> {
            UserAgent ua = new UserAgent();
            ua.setUserAgentString(userAgentString);
            try {
                coreDao.create(ua);
            } catch (Exception ex) {
                LOG.error("can't create new user agent.", ex);
            }
            return ua;
        });
        return userAgent;
    }
}
