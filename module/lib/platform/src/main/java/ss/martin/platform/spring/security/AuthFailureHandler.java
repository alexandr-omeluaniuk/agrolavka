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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ss.martin.platform.exception.SubscriptionHasExpiredException;
import ss.martin.platform.wrapper.RESTResponse;

/**
 * Authentication failure handler.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest hsr, HttpServletResponse hsr1,
            AuthenticationException ae) throws IOException, ServletException {
        hsr1.setStatus(HttpStatus.UNAUTHORIZED.value());
        RESTResponse failback = new RESTResponse(false, ae.getMessage());
        if (ae instanceof UsernameNotFoundException) {
            failback.setCode("1");
        } else if (ae instanceof BadCredentialsException) {
            failback.setCode("2");
        } else if (ae instanceof DisabledException) {
            failback.setCode("3");
        } else if (ae instanceof SubscriptionHasExpiredException) {
            failback.setCode("4");
        }
        hsr1.getOutputStream().println(new ObjectMapper().writeValueAsString(failback));
    }
}
