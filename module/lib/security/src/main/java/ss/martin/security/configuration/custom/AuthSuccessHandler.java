package ss.martin.security.configuration.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ss.martin.security.api.SecurityService;
import ss.martin.security.configuration.jwt.JwtTokenUtil;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;

/**
 * Authentication success handler.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthSuccessHandler implements AuthenticationSuccessHandler {
    /** Security service. */
    @Autowired
    private SecurityService securityService;
    /** JWT token utility. */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest hsr, HttpServletResponse hsr1,
            Authentication a) throws IOException, ServletException {
        hsr1.setStatus(HttpStatus.OK.value());
        UserPrincipal principal = SecurityContext.principal();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage("Welcome to Martin platform");
        principal.setUserAgent(securityService.getUserAgent(hsr));
        loginResponse.setJwt(jwtTokenUtil.generateToken(principal));
        hsr1.getOutputStream().println(new ObjectMapper().writeValueAsString(loginResponse));
    }
}
