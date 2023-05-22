package ss.martin.notification.push.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.martin.notification.push.firebase.api.GoogleCredentialsProvider;
import ss.martin.notification.push.firebase.api.MessagingAppProvider;

/**
 * Firebase messaging app provider.
 * @author alex
 */
@Service
class FirebaseMessagingAppProvider implements MessagingAppProvider {
    
    private static final Logger LOG = LoggerFactory.getLogger(FirebaseMessagingAppProvider.class);
    
    @Autowired
    private GoogleCredentialsProvider credentialsProvider;
    
    @PostConstruct
    protected void init() {
        if (FirebaseApp.getApps().isEmpty()) {
            credentialsProvider.get().ifPresent((credentials) -> {
                final var options = FirebaseOptions.builder().setCredentials(credentials).build();
                FirebaseApp.initializeApp(options);
                LOG.info("Firebase initialization completed...");
            });
        }
    }

    @Override
    public FirebaseMessaging get() {
        return FirebaseMessaging.getInstance();
    }
}
