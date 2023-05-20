package ss.martin.security.configuration.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ss.entity.security.UserAgent;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.configuration.jwt.JwtTokenUtil;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;
import ss.martin.security.dao.UserDao;

/**
 * Authentication success handler.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthSuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CoreDao coreDao;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest hsr, HttpServletResponse hsr1, Authentication a) 
            throws IOException, ServletException {
        hsr1.setStatus(HttpStatus.OK.value());
        UserPrincipal principal = SecurityContext.principal();
        principal.setUserAgent(getUserAgent(hsr));
        hsr1.getOutputStream().println(new ObjectMapper().writeValueAsString(
                new LoginResponse(jwtTokenUtil.generateToken(principal), "Welcome")
        ));
    }
    
    private UserAgent getUserAgent(HttpServletRequest httpRequest) {
        UserPrincipal principal = SecurityContext.principal();
        String userAgentString = httpRequest.getHeader("User-Agent");
        List<UserAgent> userAgents = userDao.getUserAgents(principal.getUser());
        UserAgent userAgent = userAgents.stream().filter(ua -> {
            return userAgentString.equals(ua.getUserAgentString());
        }).findFirst().map(ua -> ua).orElseGet(() -> {
            UserAgent ua = new UserAgent();
            ua.setUserAgentString(userAgentString);
            coreDao.create(ua);
            return ua;
        });
        return userAgent;
    }
}
