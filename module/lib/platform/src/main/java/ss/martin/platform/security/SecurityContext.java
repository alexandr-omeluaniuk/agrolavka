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
package ss.martin.platform.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ss.entity.martin.Subscription;
import ss.entity.martin.SystemUser;
import ss.martin.platform.spring.security.UserPrincipal;

/**
 * Security context.
 * Provided access to current security context.
 * @author ss
 */
public class SecurityContext {
    /**
     * Get current user.
     * @return current user.
     */
    public static synchronized SystemUser currentUser() {
        UserPrincipal userPrincipal = principal();
        return userPrincipal == null ? null : userPrincipal.getUser();
    }
    /**
     * Get user principal.
     * @return user principal.
     */
    public static synchronized UserPrincipal principal() {
        Object auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) auth;
            return userPrincipal;
        } else {
            return null;
        }
    }
    /**
     * Get current user subscription.
     * @return subscription.
     */
    public static synchronized Subscription subscription() {
        return currentUser().getSubscription();
    }
    /**
     * Create principal for user.
     * @param user user.
     * @return principal.
     */
    public static UserPrincipal createPrincipal(SystemUser user) {
        GrantedAuthority ga = new SimpleGrantedAuthority(user.getStandardRole().name());
        List<GrantedAuthority> gaList = new ArrayList<>();
        gaList.add(ga);
        UserPrincipal principal = new UserPrincipal(user.getEmail(), user.getPassword(), gaList);
        principal.setUser(user);
        return principal;
    }
}
