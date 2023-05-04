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

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ss.entity.martin.SystemUser;
import ss.martin.platform.dao.UserDAO;
import ss.martin.platform.exception.SubscriptionHasExpiredException;
import ss.martin.platform.security.SecurityContext;
import ss.martin.platform.security.SystemUserStatus;

/**
 * Authentication provider.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthManager implements AuthenticationManager {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AuthManager.class);
    /** Password encoder. */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /** SystemUser DAO. */
    @Autowired
    private UserDAO userDAO;
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = String.valueOf(auth.getPrincipal());
        String password = String.valueOf(auth.getCredentials());
        SystemUser user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        if (SystemUserStatus.ACTIVE != user.getStatus()) {
            throw new DisabledException("User is deactivated: " + username);
        }
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password: " + password);
        }
        if (user.getSubscription().getExpirationDate().before(new Date())) {
            throw new SubscriptionHasExpiredException(user.getSubscription());
        }
        LOG.info("successfull authentication for [" + user + "] completed...");
        return SecurityContext.createPrincipal(user);
    }
}
