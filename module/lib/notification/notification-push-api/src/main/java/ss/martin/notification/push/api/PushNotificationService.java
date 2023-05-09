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
     */
    String sendPersonalNotification(String clientToken, PushNotification notification);
    
    /**
     * Send topic notification.
     * @param notification notification.
     * @param topic topic.
     * @return Firebase response.
     */
    String sendTopicNotification(String topic, PushNotification notification);
    
    /**
     * Subscribe user agents to topic.
     * @param topic topic.
     * @param clientTokens users.
     */
    void subscribeToTopic(String topic, Set<String> clientTokens);
    
    /**
     * Unsubscribe user agents from topic.
     * @param topic topic.
     * @param clientTokens user agents.
     */
    void unsubscribeFromTopic(String topic, Set<String> clientTokens);
}
