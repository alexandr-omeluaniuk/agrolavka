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

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ss.martin.platform.spring.config.PlatformConfiguration;

/**
 * Authentication entry point.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthEntryPoint implements AuthenticationEntryPoint {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AuthEntryPoint.class);
    /** Platform configuration. */
    @Autowired
    private PlatformConfiguration configuration;
    @Override
    public void commence(HttpServletRequest hsr, HttpServletResponse hsr1,
            AuthenticationException ae) throws IOException, ServletException {
        String contentType = hsr.getHeader("Accept");
        if (LOG.isDebugEnabled()) {
            LOG.debug(ae.getMessage());
            LOG.debug(hsr.getRemoteAddr());
            LOG.debug("Accept: " + contentType);
        }
        if (contentType != null && contentType.contains("application/json")) {  // Ajax request detected
            hsr1.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            hsr1.sendRedirect(configuration.getNavigation().getLoginPage());
        }
    }
}
