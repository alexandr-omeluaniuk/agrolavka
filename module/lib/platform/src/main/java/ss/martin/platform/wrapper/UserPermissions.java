/*
 * The MIT License
 *
 * Copyright 2020 ss.
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
package ss.martin.platform.wrapper;

import ss.entity.martin.Subscription;
import ss.entity.martin.UserAgent;
import ss.martin.core.constants.StandardRole;

/**
 * User permissions for UI.
 * @author ss
 */
public class UserPermissions {
    /** Subscription. */
    private Subscription subscription;
    /** User full name. */
    private String fullname;
    /** User ID. */
    private Long userId;
    /** STandard role. */
    private StandardRole standardRole;
    /** Has Firebase token. */
    private UserAgent userAgent;
    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }
    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    /**
     * @return the subscription
     */
    public Subscription getSubscription() {
        return subscription;
    }
    /**
     * @param subscription the subscription to set
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    /**
     * @return the standardRole
     */
    public StandardRole getStandardRole() {
        return standardRole;
    }
    /**
     * @param standardRole the standardRole to set
     */
    public void setStandardRole(StandardRole standardRole) {
        this.standardRole = standardRole;
    }
    /**
     * @return the userAgent
     */
    public UserAgent getUserAgent() {
        return userAgent;
    }
    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }
}
