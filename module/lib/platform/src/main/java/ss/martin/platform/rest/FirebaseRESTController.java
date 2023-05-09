/*
 * The MIT License
 *
 * Copyright 2021 alex.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FirebaseRESTController {
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    /** Firebase client. */
    @Autowired
    private PushNotificationService pushNotificationService;
    /**
     * Subscribe Firebase notifications.
     * @param topic Firebase notifications topic.
     * @param firebaseToken firebase token.
     * @param userAgentString user agent string.
     * @throws Exception error.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RequestMapping(value = "/topic/subscribe/{firebaseToken}/{topic}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void subscribeToTopic(@PathVariable("topic") String topic,
            final @PathVariable("firebaseToken") String firebaseToken,
            final @RequestHeader(value = "User-Agent") String userAgentString) throws Exception {
        UserAgent userAgent = SecurityContext.principal().getUserAgent();
        userAgent = coreDAO.findById(userAgent.getId(), UserAgent.class);
        Optional<NotificationTopicSubscription> subs = userAgent.getNotificationSubscriptions().stream().filter(s -> {
            return topic.equals(s.getTopic());
        }).findFirst();
        if (SecurityContext.currentUser().equals(userAgent.getCreatedBy()) && subs.isEmpty()) {
            userAgent.setFirebaseToken(firebaseToken);
            userAgent.setUserAgentString(userAgentString);
            coreDAO.update(userAgent);
            NotificationTopicSubscription notificationSubscription = new NotificationTopicSubscription();
            notificationSubscription.setTopic(topic);
            notificationSubscription.setUserAgent(userAgent);
            coreDAO.create(notificationSubscription);
            pushNotificationService.subscribeToTopic(topic, new HashSet<>(Arrays.asList(userAgent.getFirebaseToken())));
        }
    }
    /**
     * Unsubscribe Firebase notifications.
     * @param topic Firebase notifications topic.
     * @param firebaseToken firebase token.
     * @param userAgentString user agent string.
     * @throws Exception error.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RequestMapping(value = "/topic/unsubscribe/{firebaseToken}/{topic}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void unsubscribeFirebaseNotifications(@PathVariable("topic") String topic,
            final @PathVariable("firebaseToken") String firebaseToken,
            final @RequestHeader(value = "User-Agent") String userAgentString) throws Exception {
        UserAgent userAgent = SecurityContext.principal().getUserAgent();
        Optional<NotificationTopicSubscription> subs = userAgent.getNotificationSubscriptions().stream().filter(s -> {
            return topic.equals(s.getTopic());
        }).findFirst();
        if (subs.isPresent()) {
            pushNotificationService.unsubscribeFromTopic(topic, new HashSet<>(Arrays.asList(userAgent.getFirebaseToken())));
            coreDAO.delete(subs.get().getId(), NotificationTopicSubscription.class);
        }
    }
}
