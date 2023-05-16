package ss.martin.security.configuration.custom;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ss.martin.security.configuration.external.NavigationConfiguration;

/**
 * Authentication entry point.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthEntryPoint implements AuthenticationEntryPoint {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AuthEntryPoint.class);
    /** Platform configuration. */
    @Autowired
    private NavigationConfiguration configuration;
    @Override
    public void commence(HttpServletRequest hsr, HttpServletResponse hsr1,
            AuthenticationException ae) throws IOException, ServletException {
        String contentType = hsr.getHeader("Accept");
        if (LOG.isDebugEnabled()) {
            LOG.debug(ae.getMessage());
            LOG.debug(hsr.getRemoteAddr());
            LOG.debug("Accept: " + contentType);
        }
        if (contentType != null && contentType.contains("application/json")) {  // Ajax request detected
            hsr1.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            hsr1.sendRedirect(configuration.loginPage());
        }
    }
}
