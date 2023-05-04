/*
 * Copyright (C) 2018 Wisent Media
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ss.martin.platform.spring.security;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ss.entity.martin.SystemUser;
import ss.entity.martin.UserAgent;

/**
 * SystemUser principal.
 * @author Alexandr Omeluaniuk
 */
public class UserPrincipal extends UsernamePasswordAuthenticationToken {
    /** SystemUser. */
    private SystemUser user;
    /** User agent of logged in user. */
    private UserAgent userAgent;
    /**
     * Constructor.
     * @param username - username.
     * @param password - password.
     * @param authorities - authorities.
     */
    public UserPrincipal(String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    /**
     * @return the user
     */
    public SystemUser getUser() {
        return user;
    }
    /**
     * @param user the user to set
     */
    public void setUser(SystemUser user) {
        this.user = user;
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
