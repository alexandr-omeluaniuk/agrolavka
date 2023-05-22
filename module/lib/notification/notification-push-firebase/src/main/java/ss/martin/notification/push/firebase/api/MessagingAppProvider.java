package ss.martin.notification.push.firebase.api;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Firebase messaging app provider.
 * @author alex
 */
@FunctionalInterface
public interface MessagingAppProvider {
    
    /**
     * Get firebase messaging app.
     * @return app.
     */
    FirebaseMessaging get();
}
