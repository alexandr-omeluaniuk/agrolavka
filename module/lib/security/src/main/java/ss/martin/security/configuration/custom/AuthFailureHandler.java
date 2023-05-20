package ss.martin.security.configuration.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ss.martin.base.lang.ThrowingRunnable;
import ss.martin.security.constants.LoginFaultCode;
import ss.martin.security.exception.SubscriptionHasExpiredException;
import ss.martin.security.model.RestResponse;

/**
 * Authentication failure handler.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthFailureHandler implements AuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(
            final HttpServletRequest request, 
            final HttpServletResponse response, 
            final AuthenticationException ae
    ) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        final var message = Optional.ofNullable(getFaultCode(ae))
            .map(faultCode -> new RestResponse(false, faultCode.getMessage(), faultCode.getCode()))
            .orElseGet(() -> new RestResponse(false, "Unknown error"));
        sendResponse(message, response);
    }
    
    private LoginFaultCode getFaultCode(final AuthenticationException ae) {
        if (ae instanceof UsernameNotFoundException) {
            return LoginFaultCode.WRONG_USERNAME;
        } else if (ae instanceof BadCredentialsException) {
            return LoginFaultCode.BAD_CREDENTIALS;
        } else if (ae instanceof DisabledException) {
            return LoginFaultCode.DEACTIVATED;
        } else if (ae instanceof SubscriptionHasExpiredException) {
            return LoginFaultCode.SUBSCRIPTION_EXPIRED;
        }
        return null;
    }
    
    private void sendResponse(final RestResponse message, final HttpServletResponse response) {
        ThrowingRunnable runnable = () -> response.getOutputStream()
            .println(new ObjectMapper().writeValueAsString(message));
        runnable.run();
    }
}
