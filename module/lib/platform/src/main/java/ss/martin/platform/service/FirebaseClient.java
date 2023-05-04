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
package ss.martin.platform.service;

import java.util.Set;
import ss.entity.martin.UserAgent;
import ss.martin.platform.wrapper.PushNotification;

/**
 * Firebase client.
 * @author alex
 */
public interface FirebaseClient {
    /**
     * Send personal notification.
     * @param notification notification.
     * @param userAgent user agent.
     * @return Firebase response.
     * @throws Exception error.
     */
    String sendPersonalNotification(PushNotification notification, UserAgent userAgent) throws Exception;
    /**
     * Send topic notification.
     * @param notification notification.
     * @param topic topic.
     * @return Firebase response.
     * @throws Exception error.
     */
    String sendTopicNotification(PushNotification notification, String topic) throws Exception;
    /**
     * Subscribe user agents to topic.
     * @param topic topic.
     * @param userAgents users.
     * @throws Exception error.
     */
    void subscribeToTopic(String topic, Set<UserAgent> userAgents) throws Exception;
    /**
     * Unsubscribe user agents from topic.
     * @param topic topic.
     * @param userAgents user agents.
     * @throws Exception error.
     */
    void unsubscribeFromTopic(String topic, Set<UserAgent> userAgents) throws Exception;
}
