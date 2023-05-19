package ss.martin.security.test;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.security.configuration.jwt.JwtTokenUtil;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.test.util.DataFactory;
import ss.martin.test.AbstractComponentTest;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import ss.martin.core.constants.StandardRole;
import ss.martin.security.configuration.jwt.JwtTokenDateFactory;

public class JwtTokenTest extends AbstractComponentTest {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @MockBean
    private JwtTokenDateFactory dateFactory;
    
    @Test
    @DisplayName("JWT token validation test")
    public void testValidateToken() {
        final var validEmail = "jwt.test@test.com";
        final var invalidEmail = "jwt.invalid@test.com";
        final var user = DataFactory.generateSystemUser(validEmail, "Alan Gray");
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_USER);
        final var principal = SecurityContext.createPrincipal(user);
        
        Mockito.when(dateFactory.getIssuedAtDate()).thenReturn(new Date());
        Mockito.when(dateFactory.getExpiredAtDate()).thenReturn(
            new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1))
        );
        
        final var jwt = jwtTokenUtil.generateToken(principal);
        
        assertTrue(jwtTokenUtil.validateToken(jwt, validEmail));
        assertFalse(jwtTokenUtil.validateToken(jwt, invalidEmail));
        
        Mockito.when(dateFactory.getIssuedAtDate()).thenReturn(
            new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2))
        );
        Mockito.when(dateFactory.getExpiredAtDate()).thenReturn(
            new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
        );
        
        final var expiredJwt = jwtTokenUtil.generateToken(principal);
        
        final var ex1 = assertThrows(ExpiredJwtException.class, 
            () -> jwtTokenUtil.validateToken(expiredJwt, validEmail)
        );
        assertNotNull(ex1);
        final var ex2 = assertThrows(ExpiredJwtException.class, 
            () -> jwtTokenUtil.validateToken(expiredJwt, invalidEmail)
        );
        assertNotNull(ex2);
    }
}
