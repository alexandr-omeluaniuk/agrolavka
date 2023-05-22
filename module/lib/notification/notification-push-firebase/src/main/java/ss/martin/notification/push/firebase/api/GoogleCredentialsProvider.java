package ss.martin.notification.push.firebase.api;

import com.google.auth.oauth2.GoogleCredentials;
import java.util.Optional;

/**
 * Google credentials provider.
 * @author alex
 */
@FunctionalInterface
public interface GoogleCredentialsProvider {
    
    /**
     * Get Google credentials.
     * @return Google credentials.
     */
    Optional<GoogleCredentials> get();
}
