package ss.agrolavka.test;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ss.agrolavka.constants.SiteConstants;
import ss.martin.core.constants.StandardRole;
import static org.junit.jupiter.api.Assertions.*;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;


public class PublicRestControllerTest extends AbstractAgrolavkaMvcTest {
    
    @Test
    public void testSearch() {
        withAuthorization(jwt(StandardRole.ROLE_SUBSCRIPTION_USER), () -> {
            final var responseCreate = callGet(
                SiteConstants.URL_PUBLIC + "/search?searchText=", 
                Map.class, 
                HttpStatus.OK
            );
            assertNotNull(responseCreate);
        });
    }
}
