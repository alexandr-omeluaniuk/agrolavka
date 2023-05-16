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
import ss.martin.security.model.RESTResponse;

/**
 * Logout success handler.
 * @author Alexandr Omeluaniuk
 */
@Component
class SignOutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest hsr, HttpServletResponse hsr1, Authentication a)
            throws IOException, ServletException {
        hsr1.setStatus(HttpStatus.OK.value());
        RESTResponse success = new RESTResponse(true, "Logout is OK!");
        hsr1.getOutputStream().println(new ObjectMapper().writeValueAsString(success));
    }
}
