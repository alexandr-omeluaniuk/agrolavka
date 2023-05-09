package ss.martin.notification.push.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.martin.notification.push.api.PushNotificationService;
import ss.martin.notification.push.api.model.PushNotification;
import ss.martin.notification.push.firebase.configuration.FirebaseConfiguration;

/**
 * Firebase client implementation.
 * @author alex
 */
@Service
class FirebaseSender implements PushNotificationService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(FirebaseSender.class);
    /** Platform configuration. */
    @Autowired
    private FirebaseConfiguration configuration;
    
    @PostConstruct
    protected void init() {
        Optional.ofNullable(configuration.firebaseConfigFilePath()).ifPresent(firebaseConf -> {
            if (FirebaseApp.getApps().isEmpty()) {
                try (InputStream serviceAccount = readGoogleCredentials(firebaseConf)) {
                    FirebaseOptions options = FirebaseOptions.builder()
                          .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
                    FirebaseApp.initializeApp(options);
                    LOG.info("Firebase initialization completed...");
                } catch (IOException e) {
                    LOG.error("Firebase credentials loading error!", e);
                }
            }
        });
    }

    @Override
    public String sendPersonalNotification(String clientToken, PushNotification notification) throws Exception {
        final var message = Message.builder().setToken(clientToken).setWebpushConfig(
                WebpushConfig.builder().putHeader("ttl", notification.ttlInSeconds())
                        .setNotification(createWebPushBuilder(notification).build()).build()
        ).build();
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    @Override
    public String sendTopicNotification(String topic, PushNotification notification) throws Exception {
        final var message = Message.builder().setTopic(topic).setWebpushConfig(
                WebpushConfig.builder().putHeader("ttl", notification.ttlInSeconds())
                        .setFcmOptions(WebpushFcmOptions.withLink(notification.clickAction()))
                        .setNotification(createWebPushBuilder(notification).build()).build()
        ).build();
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    @Override
    public void subscribeToTopic(final String topic, final Set<String> clientTokens) throws Exception {
        FirebaseMessaging.getInstance().subscribeToTopic(new ArrayList<>(clientTokens), topic);
    }
    
    @Override
    public void unsubscribeFromTopic(final String topic, final Set<String> clientTokens) throws Exception {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(new ArrayList<>(clientTokens), topic);
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
    
    private InputStream readGoogleCredentials(final String firebaseConf) throws IOException {
        final var path = Paths.get(firebaseConf);
        if (Files.exists(path)) {
            return Files.newInputStream(path);
        }
        return Optional.ofNullable(getClass().getResourceAsStream(firebaseConf))
                .orElseThrow(() -> new IOException("Firebase configuration file is not found"));
    }
}
