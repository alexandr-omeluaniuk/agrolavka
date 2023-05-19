package ss.martin.security.configuration.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import ss.martin.security.model.RestResponse;

/**
 * Logout success handler.
 * @author Alexandr Omeluaniuk
 */
@Component
class SignOutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(
        final HttpServletRequest request, 
        final HttpServletResponse response, 
        final Authentication auth
    ) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.getOutputStream().println(new ObjectMapper().writeValueAsString(new RestResponse()));
    }
}
