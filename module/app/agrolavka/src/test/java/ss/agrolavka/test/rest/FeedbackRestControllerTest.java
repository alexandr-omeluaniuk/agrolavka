package ss.agrolavka.test.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import ss.entity.agrolavka.Feedback;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.model.EntitySearchResponse;

public class FeedbackRestControllerTest extends AbstractAgrolavkaMvcTest {
    
    @Test
    public void testGetListAndDelete() {
        final var feedback = coreDao.create(getFeedback());
        withAuthorization(jwt(StandardRole.ROLE_SUBSCRIPTION_USER), () -> {
            // get list
            final var returnType = new TypeReference<EntitySearchResponse<Feedback>>() { };
            final var entitiesList = callGet(SiteConstants.URL_PROTECTED + "/feedback", returnType, HttpStatus.OK);
            assertTrue(entitiesList.total() > 0);
            assertFalse(entitiesList.data().isEmpty());
            assertEquals(feedback.getId(), entitiesList.data().get(0).getId());
            
            // delete
            callDelete(SiteConstants.URL_PROTECTED + "/feedback/" + feedback.getId(), Void.class, HttpStatus.OK);
            assertTrue(coreDao.getAll(Feedback.class).isEmpty());
        });
    }
    
    private Feedback getFeedback() {
        final var feedback = new Feedback();
        feedback.setContact("some.contact@test");
        feedback.setMessage("Help me!");
        feedback.setCreated(new Date());
        return feedback;
    }
}
