package ss.martin.security.configuration.custom;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    /** Platform configuration. */
    @Autowired
    private NavigationConfiguration configuration;
    
    @Override
    public void commence(
        final HttpServletRequest request, 
        final HttpServletResponse response,
        final AuthenticationException ae
    ) throws IOException, ServletException {
        final var contentType = request.getHeader(HttpHeaders.ACCEPT);
        if (contentType != null && contentType.contains("application/json")) {
            // Ajax request detected
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.sendRedirect(configuration.loginPage());
        }
    }
}
