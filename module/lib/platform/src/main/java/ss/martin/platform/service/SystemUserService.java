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
package ss.martin.platform.service;

import ss.entity.martin.SystemUser;

/**
 * System user service.
 * @author ss
 */
public interface SystemUserService {
    /**
     * Start user registration.
     * @param systemUser system user.
     * @throws Exception error.
     */
    void startRegistration(SystemUser systemUser) throws Exception;
    /**
     * Finish user registration.
     * @param validationString validation string.
     * @param password password.
     * @throws Exception error.
     */
    void finishRegistration(String validationString, String password) throws Exception;
    /**
     * Check if super user exists.
     * If no - create it.
     */
    void superUserCheck();
    /**
     * Create system user with ROLE_SUBSCRIPTION_USER.
     * @param user user without ID.
     * @return new system user.
     * @throws Exception error.
     */
    SystemUser createSystemUser(SystemUser user) throws Exception;
}
