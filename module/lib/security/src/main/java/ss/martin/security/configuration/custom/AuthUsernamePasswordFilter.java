package ss.martin.security.configuration.custom;

import ss.martin.security.model.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ss.martin.base.lang.ThrowingSupplier;

/**
 * Custom username/password filter.
 * @author Alexandr Omeluaniuk
 */
public class AuthUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {
    /** Username. */
    private String jsonUsername;
    /** Password. */
    private String jsonPassword;
    
    @Override
    protected String obtainPassword(final HttpServletRequest request) {
        return this.jsonPassword;
    }
    
    @Override
    protected String obtainUsername(final HttpServletRequest request) {
        return this.jsonUsername;
    }
    
    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        return ((ThrowingSupplier<Authentication>) () -> {
            final var loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            this.jsonUsername = loginRequest.username();
            this.jsonPassword = loginRequest.password();
            return super.attemptAuthentication(request, response);
        }).get();
    }
}
