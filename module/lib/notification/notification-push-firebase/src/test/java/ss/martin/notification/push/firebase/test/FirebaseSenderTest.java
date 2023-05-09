package ss.martin.notification.push.firebase.test;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.notification.push.api.PushNotificationService;
import ss.martin.notification.push.api.model.PushNotification;
import ss.martin.test.AbstractComponentTest;

public class FirebaseSenderTest extends AbstractComponentTest {
    
    private static final String CLIENT_TOKEN_EXAMPLE = "eDGIJ4phSYEmdQ0T8dA_kE:APA91bHWwrMhRFb_ftb8spC0U9g8jsgmfnfrt47HIPScisvsIUmVoILQtU6ISwGHf7foCo5S0POhMbfoizszOHhiB_Crf3zGdPnAyCN04wOy8wdSyDj5-Pey2IUztaS-E7owzjepAdlc";
    private static final String TEST_TOPIC = "TEST_TOPIC";
    private static final Set<String> CLIENT_TOKENS = new HashSet<>(Arrays.asList(CLIENT_TOKEN_EXAMPLE));
    
    @Autowired
    private PushNotificationService pushNotificationService;
    
    @Test
    public void testSubscribeToTopic() {
        assertDoesNotThrow(() -> pushNotificationService.subscribeToTopic(TEST_TOPIC, CLIENT_TOKENS));
    }
    
    @Test
    public void testSendTopicNotification() {
        assertDoesNotThrow(() -> pushNotificationService.sendTopicNotification(TEST_TOPIC, new PushNotification(
                "Test title",
                "Test body",
                null,
                "https://some.domain/somelink",
                "Click me",
                "300",
                null
        )));
    }
    
    @Test
    public void testSendPersonalNotification() {
        final var exception = assertThrows(ExecutionException.class, () -> pushNotificationService.sendPersonalNotification(
                CLIENT_TOKEN_EXAMPLE,
                new PushNotification(
                    "Test title",
                    "Test body",
                    null,
                    "https://some.domain/somelink",
                    "Click me",
                    "300",
                    null
                )
        )).getCause();
        assertEquals("SenderId mismatch", exception.getMessage());
        assertTrue(exception instanceof FirebaseMessagingException);
    }
    
    @Test
    public void testUnsubscribeFromTopic() {
        assertDoesNotThrow(() -> pushNotificationService.unsubscribeFromTopic(TEST_TOPIC, CLIENT_TOKENS));
    }
}
