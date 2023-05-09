package ss.martin.notification.push.api;

import java.util.Set;
import ss.martin.notification.push.api.model.PushNotification;

/**
 * Push notification service API.
 * @author alex
 */
public interface PushNotificationService {
    /**
     * Send personal notification.
     * @param notification notification.
     * @param clientToken Firebase client token.
     * @return Firebase response.
     * @throws Exception error.
     */
    String sendPersonalNotification(String clientToken, PushNotification notification) throws Exception;
    
    /**
     * Send topic notification.
     * @param notification notification.
     * @param topic topic.
     * @return Firebase response.
     * @throws Exception error.
     */
    String sendTopicNotification(String topic, PushNotification notification) throws Exception;
    
    /**
     * Subscribe user agents to topic.
     * @param topic topic.
     * @param clientTokens users.
     * @throws Exception error.
     */
    void subscribeToTopic(String topic, Set<String> clientTokens) throws Exception;
    
    /**
     * Unsubscribe user agents from topic.
     * @param topic topic.
     * @param clientTokens user agents.
     * @throws Exception error.
     */
    void unsubscribeFromTopic(String topic, Set<String> clientTokens) throws Exception;
}
