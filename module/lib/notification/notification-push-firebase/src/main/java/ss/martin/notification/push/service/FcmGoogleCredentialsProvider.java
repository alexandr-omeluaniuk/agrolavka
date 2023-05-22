package ss.martin.notification.push.service;

import com.google.auth.oauth2.GoogleCredentials;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.martin.base.lang.ThrowingSupplier;
import ss.martin.notification.push.firebase.api.GoogleCredentialsProvider;
import ss.martin.notification.push.firebase.configuration.FirebaseConfiguration;

/**
 * FCM Google credentials provider.
 * @author alex
 */
@Service
class FcmGoogleCredentialsProvider implements GoogleCredentialsProvider {
    
    @Autowired
    private FirebaseConfiguration configuration;

    @Override
    public Optional<GoogleCredentials> get() {
        final var credentials = Optional.ofNullable(configuration.firebaseConfigFilePath()).map(confPath -> { 
            return ((ThrowingSupplier<GoogleCredentials>) () -> {
                final var path = Paths.get(confPath);
                if (Files.exists(path)) {
                    return GoogleCredentials.fromStream(Files.newInputStream(path));
                }
                return null;
            }).get();
        }).orElse(null);
        return Optional.ofNullable(credentials);
    }
}
