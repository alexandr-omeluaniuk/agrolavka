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
import org.springframework.util.function.ThrowingConsumer;
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
            final HttpServletRequest hsr, 
            final HttpServletResponse hsr1, 
            final AuthenticationException ae
    ) throws IOException, ServletException {
        hsr1.setStatus(HttpStatus.UNAUTHORIZED.value());
        LoginFaultCode code = null;
        if (ae instanceof UsernameNotFoundException) {
            code = LoginFaultCode.WRONG_USERNAME;
        } else if (ae instanceof BadCredentialsException) {
            code = LoginFaultCode.BAD_CREDENTIALS;
        } else if (ae instanceof DisabledException) {
            code = LoginFaultCode.DEACTIVATED;
        } else if (ae instanceof SubscriptionHasExpiredException) {
            code = LoginFaultCode.SUBSCRIPTION_EXPIRED;
        }
        ThrowingConsumer<LoginFaultCode> runnable = (faultCode) -> hsr1.getOutputStream()
            .println(new ObjectMapper().writeValueAsString(
                new RestResponse(false, faultCode.getMessage(), faultCode.getCode())
        ));
        Optional.ofNullable(code).ifPresent(runnable);
    }
}
