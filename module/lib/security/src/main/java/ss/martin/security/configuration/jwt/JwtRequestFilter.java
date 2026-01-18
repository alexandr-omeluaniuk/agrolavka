package ss.martin.security.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.base.lang.ThrowingFunction;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static final List<String> blacklist = new ArrayList<>();

    static {
        blacklist.add("POST::/api/route");
        blacklist.add("POST::/app");
        blacklist.add("POST::/api");
        blacklist.add("POST::/_next/server");
        blacklist.add("POST::/_next");
        blacklist.add("POST::/error");
        blacklist.add("GET::/shops/php_info.php");
        blacklist.add("GET::/shops/phptest.php3");
        blacklist.add("GET::/shops/info.php3");
        blacklist.add("GET::/shops/phpinfo.php3");
    }
    
    @Override
    protected void doFilterInternal(
        final HttpServletRequest request, 
        final HttpServletResponse response, 
        final FilterChain chain
    ) throws ServletException, IOException {
        final var match = request.getMethod() + "::" + request.getServletPath();
        if (blacklist.contains(match)) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY);
        } else {
            handleAuthorizationHeader(request);
            chain.doFilter(request, response);
        }
    }
    
    private void handleAuthorizationHeader(final HttpServletRequest request) {
        try {
            Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).ifPresent(header -> {
                final var jwtToken = header.replace(HEADER_BEARER, "");
                setPrincipalFromJwtToken(jwtToken);
            });
        } catch (final Exception e) {
            LOG.warn("Can't handle Authorization header! Reason: " + e.getMessage());
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
            final var restoredPrincipal = SecurityContext.createPrincipal(systemUser);
            return restoredPrincipal;
        };
        final var principal = jwtTokenUtil.getClaimFromToken(jwtToken, principalFunc);
        SecurityContextHolder.getContext().setAuthentication(principal);
    }
}
