package ss.martin.platform.test;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ss.entity.martin.Subscription;
import ss.martin.security.constants.PlatformUrl;
import ss.martin.test.AbstractMvcTest;
import static org.junit.jupiter.api.Assertions.*;
import ss.martin.security.test.DataFactory;

public class EntityRestControllerTest extends AbstractMvcTest {
    
    @Test
    public void testCreate_WithoutAuth() {
        callPost(
            PlatformUrl.ENTITY_URL + "/" + Subscription.class.getName(), 
            DataFactory.generateSubscription("hello@test.test"), 
            Subscription.class, 
            HttpStatus.FOUND
        );
    }
    
    @Test
    public void testCreate() {
        final var response = callPost(
            PlatformUrl.ENTITY_URL + "/" + Subscription.class.getName(), 
            DataFactory.generateSubscription("hello@test.test"), 
            Subscription.class, 
            HttpStatus.OK
        );
        
        assertNotNull(response);
        assertNotNull(response.getId());
    }
}
