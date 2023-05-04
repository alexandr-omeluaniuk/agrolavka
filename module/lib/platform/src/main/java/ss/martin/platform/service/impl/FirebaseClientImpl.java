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
package ss.martin.platform.service.impl;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.entity.martin.UserAgent;
import ss.martin.platform.exception.PlatformException;
import ss.martin.platform.service.FirebaseClient;
import ss.martin.platform.spring.config.PlatformConfiguration;
import ss.martin.platform.wrapper.PushNotification;

/**
 * Firebase client implementation.
 * @author alex
 */
@Service
class FirebaseClientImpl implements FirebaseClient {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(FirebaseClientImpl.class);
    /** Platform configuration. */
    @Autowired
    private PlatformConfiguration configuration;
    /**
     * Initialization.
     */
    @PostConstruct
    protected void init() {
        if (configuration.getFirebaseConfigFilePath() != null && !configuration.getFirebaseConfigFilePath().isBlank()) {
            if (FirebaseApp.getApps().isEmpty()) {
                Path p = Paths.get(configuration.getFirebaseConfigFilePath());
                try (InputStream serviceAccount = Files.newInputStream(p)) {
                    FirebaseOptions options = FirebaseOptions.builder()
                          .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
                    FirebaseApp.initializeApp(options);
                    LOG.info("Firebase initialization completed...");
                } catch (IOException e) {
                    LOG.error("Firebase credentials loading error!", e);
                }
            }
        }
    }

    @Override
    public String sendPersonalNotification(PushNotification notification, UserAgent userAgent) throws Exception {
        if (userAgent.getFirebaseToken() == null || userAgent.getFirebaseToken().isBlank()) {
            throw new PlatformException(userAgent + " has not Firebase token!");
        }
        Message message = Message.builder().setToken(userAgent.getFirebaseToken()).setWebpushConfig(
                WebpushConfig.builder().putHeader("ttl", notification.getTtlInSeconds())
                        .setNotification(createWebPushBuilder(notification).build()).build()
        ).build();
        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        return response;
    }

    @Override
    public String sendTopicNotification(PushNotification notification, String topic) throws Exception {
        Message message = Message.builder().setTopic(topic).setWebpushConfig(
                WebpushConfig.builder().putHeader("ttl", notification.getTtlInSeconds())
                        .setFcmOptions(WebpushFcmOptions.withLink(notification.getClickAction()))
                        .setNotification(createWebPushBuilder(notification).build()).build()
        ).build();
        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        return response;
    }

    @Override
    public void subscribeToTopic(String topic, Set<UserAgent> userAgents) throws Exception {
        for (UserAgent userAgent : userAgents) {
            if (userAgent.getFirebaseToken() != null && !userAgent.getFirebaseToken().isBlank()) {
                FirebaseMessaging.getInstance().subscribeToTopic(
                        Collections.singletonList(userAgent.getFirebaseToken()), topic);
            }
        }
    }
    
    @Override
    public void unsubscribeFromTopic(String topic, Set<UserAgent> userAgents) throws Exception {
        for (UserAgent userAgent : userAgents) {
            if (userAgent.getFirebaseToken() != null && !userAgent.getFirebaseToken().isBlank()) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(
                        Collections.singletonList(userAgent.getFirebaseToken()), topic);
            }
        }
    }
    // ======================================== PRIVATE ===============================================================
    /**
     * Create builder from notification.
     * @param notification notification.
     * @return builder.
     */
    private WebpushNotification.Builder createWebPushBuilder(PushNotification notification) throws Exception {
        WebpushNotification.Builder builder = WebpushNotification.builder();
        builder.setData(notification.getData());
        builder.addAction(
                new WebpushNotification.Action(notification.getClickAction(), notification.getClickActionLabel())
        ).setVibrate(new int[] {200, 100, 200, 100, 200, 100, 200, 100, 200})
                .setBadge(notification.getIcon())
                .setIcon(notification.getIcon())
                .setTitle(notification.getTitle())
                .setRequireInteraction(true)
                .setBody(notification.getBody());
        return builder;
    }
}
