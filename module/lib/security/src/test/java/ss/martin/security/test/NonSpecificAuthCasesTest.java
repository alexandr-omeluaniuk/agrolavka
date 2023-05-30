
package ss.martin.security.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import ss.martin.security.configuration.external.NavigationConfiguration;
import ss.martin.security.dao.UserDao;
import ss.martin.security.model.LoginRequest;
import ss.martin.security.model.RestResponse;
import ss.martin.security.test.util.TestConstants;
import ss.martin.test.AbstractMvcTest;

public class NonSpecificAuthCasesTest extends AbstractMvcTest {
    
    @Autowired
    private NavigationConfiguration navigationConfiguration;
    
    @MockBean
    private UserDao userDao;
    
    @Test
    @DisplayName("Unknown auth error")
    public void testUnknownAuthError() {
        Mockito.when(userDao.findByUsername(Mockito.any())).thenThrow(RememberMeAuthenticationException.class);
        final var response = callPost(navigationConfiguration.login(), new LoginRequest("Bony", "123"), RestResponse.class, HttpStatus.UNAUTHORIZED);
        
        assertFalse(response.success());
        assertEquals("Unknown error", response.message());
    }
    
    @Test
    @DisplayName("Test an Unauthorized error http codes")
    public void testUnauthorizedHttpCode() {
        callGet(TestConstants.PROTECTED_RESOURCE, Void.class, HttpStatus.UNAUTHORIZED);
        
        final var acceptJsonHeader = new HttpHeaders();
        acceptJsonHeader.add(HttpHeaders.ACCEPT, "application/xml");
        withHeaders(acceptJsonHeader, () -> {
            callGet(TestConstants.PROTECTED_RESOURCE, Void.class, HttpStatus.FOUND);
        });
        
        final var acceptXmlHeader = new HttpHeaders();
        acceptXmlHeader.add(HttpHeaders.ACCEPT, "application/xml");
        withHeaders(acceptXmlHeader, () -> {
            callGet(TestConstants.PROTECTED_RESOURCE, Void.class, HttpStatus.FOUND);
        });
    }
}
