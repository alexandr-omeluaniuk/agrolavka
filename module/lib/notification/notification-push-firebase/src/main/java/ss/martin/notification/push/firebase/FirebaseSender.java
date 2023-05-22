package ss.martin.notification.push.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;
import java.util.ArrayList;
import java.util.Set;
import org.springframework.stereotype.Service;
import ss.martin.base.lang.ThrowingRunnable;
import ss.martin.base.lang.ThrowingSupplier;
import ss.martin.notification.push.api.PushNotificationService;
import ss.martin.notification.push.api.model.PushNotification;

/**
 * Firebase client implementation.
 * @author alex
 */
@Service
class FirebaseSender implements PushNotificationService {

    @Override
    public String sendPersonalNotification(final String clientToken, final PushNotification notification) {
        return ((ThrowingSupplier<String>) () -> {
            final var message = Message.builder().setToken(clientToken).setWebpushConfig(
                    WebpushConfig.builder().putHeader("ttl", notification.ttlInSeconds())
                            .setNotification(createWebPushBuilder(notification).build()).build()
            ).build();
            return FirebaseMessaging.getInstance().sendAsync(message).get();
        }).get();
    }

    @Override
    public String sendTopicNotification(final String topic, final PushNotification notification) {
        return ((ThrowingSupplier<String>) () -> {
            final var message = Message.builder().setTopic(topic).setWebpushConfig(
                    WebpushConfig.builder().putHeader("ttl", notification.ttlInSeconds())
                            .setFcmOptions(WebpushFcmOptions.withLink(notification.clickAction()))
                            .setNotification(createWebPushBuilder(notification).build()).build()
            ).build();
            return FirebaseMessaging.getInstance().sendAsync(message).get();
        }).get();
    }

    @Override
    public void subscribeToTopic(final String topic, final Set<String> clientTokens) {
        ((ThrowingRunnable) () -> 
                FirebaseMessaging.getInstance().subscribeToTopic(new ArrayList<>(clientTokens), topic)).run();
    }
    
    @Override
    public void unsubscribeFromTopic(final String topic, final Set<String> clientTokens) {
        ((ThrowingRunnable) () -> 
                FirebaseMessaging.getInstance().unsubscribeFromTopic(new ArrayList<>(clientTokens), topic)).run();
    }
    
    private WebpushNotification.Builder createWebPushBuilder(final PushNotification notification) throws Exception {
        final var builder = WebpushNotification.builder();
        builder.setData(notification.data());
        builder.addAction(
                new WebpushNotification.Action(notification.clickAction(), notification.clickActionLabel())
        ).setVibrate(new int[] {200, 100, 200, 100, 200, 100, 200, 100, 200})
                .setBadge(notification.icon())
                .setIcon(notification.icon())
                .setTitle(notification.title())
                .setRequireInteraction(true)
                .setBody(notification.body());
        return builder;
    }
}
