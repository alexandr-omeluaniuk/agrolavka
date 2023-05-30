package ss.martin.platform.test;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ss.entity.martin.Subscription;
import ss.martin.security.constants.PlatformUrl;
import static org.junit.jupiter.api.Assertions.*;
import ss.martin.core.constants.StandardRole;
import ss.martin.security.model.RestResponse;
import ss.martin.security.test.DataFactory;
import ss.martin.security.test.PlatformSecurityMvcTest;

public class EntityRestControllerTest extends PlatformSecurityMvcTest {
    
    @Test
    public void testCreate_WithoutAuth() {
        callPost(
            PlatformUrl.ENTITY_URL + "/" + Subscription.class.getName(), 
            DataFactory.generateSubscription("hello@test.test"), 
            Subscription.class, 
            HttpStatus.UNAUTHORIZED
        );
    }
    
    @Test
    public void testCRUD_Subscription() {
        final var jwt = jwt(StandardRole.ROLE_SUPER_ADMIN);
        withAuthorization(jwt, () -> {
            final var responseCreate = callPost(
                PlatformUrl.ENTITY_URL + "/" + Subscription.class.getName(), 
                DataFactory.generateSubscription("hello@test.test"), 
                Subscription.class, 
                HttpStatus.OK
            );

            assertNotNull(responseCreate);
            assertNotNull(responseCreate.getId());
            
            final var responseGet = callGet(
                PlatformUrl.ENTITY_URL + "/" + Subscription.class.getName() + "/" + responseCreate.getId(), 
                Subscription.class, 
                HttpStatus.OK
            );
            
            assertNotNull(responseGet.getId());
            
            final var newOrgName = "New name";
            responseCreate.setOrganizationName(newOrgName);
            final var responseUpdate = callPut(
                PlatformUrl.ENTITY_URL + "/" + Subscription.class.getName(), 
                responseCreate, 
                Subscription.class, 
                HttpStatus.OK
            );
            
            assertNotNull(responseCreate);
            assertEquals(newOrgName, responseUpdate.getOrganizationName());
            
            final var responseDelete = callDelete(
                PlatformUrl.ENTITY_URL + "/" + Subscription.class.getName() + "/" + responseCreate.getId(), 
                RestResponse.class, 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
            
            assertNotNull(responseDelete);
            assertNotNull(coreDao.findById(responseCreate.getId(), Subscription.class));
        });
    }
}
