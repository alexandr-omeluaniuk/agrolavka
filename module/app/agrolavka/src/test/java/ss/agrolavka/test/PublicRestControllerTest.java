package ss.agrolavka.test;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ss.agrolavka.constants.SiteConstants;
import ss.martin.core.constants.StandardRole;
import ss.martin.security.test.PlatformSecurityMvcTest;

@SpringBootTest(classes = AgrolavkaTestApplication.class)
public class PublicRestControllerTest extends PlatformSecurityMvcTest {
    
    @Test
    public void testSearch() {
        withAuthorization(jwt(StandardRole.ROLE_SUPER_ADMIN), () -> {
            final var responseCreate = callGet(
                SiteConstants.URL_PUBLIC + "/search?searchText=", 
                Map.class, 
                HttpStatus.OK
            );
        });
    }
}
