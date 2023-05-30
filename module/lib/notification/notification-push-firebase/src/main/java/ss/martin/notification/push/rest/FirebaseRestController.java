package ss.martin.notification.push.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.entity.security.NotificationTopicSubscription;
import ss.entity.security.UserAgent;
import ss.martin.core.dao.CoreDao;
import ss.martin.notification.push.api.PushNotificationService;
import ss.martin.security.context.SecurityContext;

/**
 * Firebase REST controller.
 * @author alex
 */
@RestController
@RequestMapping("/api/platform/firebase")
public class FirebaseRestController {
    /** Core DAO. */
    @Autowired
    private CoreDao coreDao;
    /** Firebase client. */
    @Autowired
    private PushNotificationService pushNotificationService;
    
    /**
     * Subscribe Firebase notifications.
     * @param topic Firebase notifications topic.
     * @param firebaseToken firebase token.
     * @param userAgentString user agent string.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RequestMapping(value = "/topic/subscribe/{firebaseToken}/{topic}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void subscribeToTopic(
        final @PathVariable("topic") String topic,
        final @PathVariable("firebaseToken") String firebaseToken,
        final @RequestHeader(value = HttpHeaders.USER_AGENT) String userAgentString
    ) {
        var userAgent = SecurityContext.principal().getUserAgent();
        userAgent = coreDao.findById(userAgent.getId(), UserAgent.class);
        final var subs = userAgent.getNotificationSubscriptions().stream().filter(s -> {
            return topic.equals(s.getTopic());
        }).findFirst();
        if (SecurityContext.currentUser().equals(userAgent.getCreatedBy()) && subs.isEmpty()) {
            userAgent.setFirebaseToken(firebaseToken);
            userAgent.setUserAgentString(userAgentString);
            coreDao.update(userAgent);
            final var notificationSubscription = new NotificationTopicSubscription();
            notificationSubscription.setTopic(topic);
            notificationSubscription.setUserAgent(userAgent);
            coreDao.create(notificationSubscription);
            pushNotificationService.subscribeToTopic(topic, new HashSet<>(Arrays.asList(userAgent.getFirebaseToken())));
        }
    }
    
    /**
     * Unsubscribe Firebase notifications.
     * @param topic Firebase notifications topic.
     * @param firebaseToken firebase token.
     * @param userAgentString user agent string.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RequestMapping(value = "/topic/unsubscribe/{firebaseToken}/{topic}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void unsubscribeFirebaseNotifications(
        final @PathVariable("topic") String topic,
        final @PathVariable("firebaseToken") String firebaseToken,
        final @RequestHeader(value = "User-Agent") String userAgentString
    ) {
        final var userAgent = SecurityContext.principal().getUserAgent();
        final var subs = userAgent.getNotificationSubscriptions().stream().filter(s -> {
            return topic.equals(s.getTopic());
        }).findFirst();
        if (subs.isPresent()) {
            final var clientTokens = new HashSet<>(Arrays.asList(userAgent.getFirebaseToken()));
            pushNotificationService.unsubscribeFromTopic(topic, clientTokens);
            coreDao.delete(subs.get().getId(), NotificationTopicSubscription.class);
        }
    }
}
