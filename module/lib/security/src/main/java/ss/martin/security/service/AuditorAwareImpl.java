package ss.martin.security.service;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ss.entity.security.SystemUser;
import ss.martin.security.context.UserPrincipal;

/**
 * Auditor aware implementation.
 * @author ss
 */
@Service
class AuditorAwareImpl implements AuditorAware<SystemUser> {
    @Override
    public Optional<SystemUser> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(UserPrincipal.class::cast).filter(UserPrincipal::isAuthenticated)
                .map(UserPrincipal::getUser);
    }
}
