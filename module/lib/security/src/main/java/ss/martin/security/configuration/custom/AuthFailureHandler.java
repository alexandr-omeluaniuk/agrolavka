package ss.martin.security.configuration.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ss.martin.security.exception.SubscriptionHasExpiredException;
import ss.martin.security.model.RestResponse;

/**
 * Authentication failure handler.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest hsr, HttpServletResponse hsr1, AuthenticationException ae) 
            throws IOException, ServletException {
        hsr1.setStatus(HttpStatus.UNAUTHORIZED.value());
        var code = "";
        if (ae instanceof UsernameNotFoundException) {
            code = "1";
        } else if (ae instanceof BadCredentialsException) {
            code = "2";
        } else if (ae instanceof DisabledException) {
            code = "3";
        } else if (ae instanceof SubscriptionHasExpiredException) {
            code = "4";
        }
        hsr1.getOutputStream().println(new ObjectMapper().writeValueAsString(
                new RestResponse(false, ae.getMessage(), code)
        ));
    }
}
