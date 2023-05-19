package ss.martin.security.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.entity.security.UserAgent;
import ss.martin.base.lang.ThrowingFunction;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;

/**
 * JWT filter.
 * @author alex
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);
    /** Header 'Bearer' string. */
    private static final String HEADER_BEARER = "Bearer ";
    /** JWT token utility. */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Override
    protected void doFilterInternal(
        final HttpServletRequest request, 
        final HttpServletResponse response, 
        final FilterChain chain
    ) throws ServletException, IOException {
        if (SecurityContext.principal() == null) {
            handleAuthorizationHeader(request);
        }
        chain.doFilter(request, response);
    }
    
    private void handleAuthorizationHeader(final HttpServletRequest request) {
        try {
            Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).ifPresent(header -> {
                if (header.startsWith(HEADER_BEARER)) {
                    final var jwtToken = header.substring(HEADER_BEARER.length());
                    setPrincipalFromJwtToken(jwtToken);
                }
            });
        } catch (final Exception e) {
            LOG.warn("Can't handle Authorization header!", e);
        }
    }
    
    private void setPrincipalFromJwtToken(final String jwtToken) {
        jwtTokenUtil.getSubject(jwtToken);  // check expiration & signature here
        ThrowingFunction<Claims, UserPrincipal> principalFunc = (claims) -> {
            final var objectMapper = new ObjectMapper();
            final var subscription = objectMapper.readValue(
                claims.get(JwtConstants.CLAIM_KEY_SUBSCRIPTION, String.class), Subscription.class);
            final var systemUser = objectMapper.readValue(
                claims.get(JwtConstants.CLAIM_KEY_SYSTEM_USER, String.class), SystemUser.class);
            systemUser.setSubscription(subscription);
            final var userAgent = objectMapper.readValue(
                claims.get(JwtConstants.CLAIM_KEY_USER_AGENT, String.class), UserAgent.class);
            final var restoredPrincipal = SecurityContext.createPrincipal(systemUser);
            restoredPrincipal.setUserAgent(userAgent);
            return restoredPrincipal;
        };
        final var principal = jwtTokenUtil.getClaimFromToken(jwtToken, principalFunc);
        SecurityContextHolder.getContext().setAuthentication(principal);
    }
}
