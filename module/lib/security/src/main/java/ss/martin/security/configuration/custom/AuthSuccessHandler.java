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
package ss.martin.security.configuration.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ss.martin.security.api.SecurityService;
import ss.martin.security.configuration.external.PlatformConfiguration;
import ss.martin.security.configuration.jwt.JwtTokenUtil;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.context.UserPrincipal;

/**
 * Authentication success handler.
 * @author Alexandr Omeluaniuk
 */
@Component
class AuthSuccessHandler implements AuthenticationSuccessHandler {
    /** Security service. */
    @Autowired
    private SecurityService securityService;
    /** Platform configuration. */
    @Autowired
    private PlatformConfiguration configuration;
    /** JWT token utility. */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest hsr, HttpServletResponse hsr1,
            Authentication a) throws IOException, ServletException {
        boolean isJWTAuthentication = configuration.getJwt() != null;
        hsr1.setStatus(HttpStatus.OK.value());
        UserPrincipal principal = SecurityContext.principal();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage("Welcome to Martin platform");
        principal.setUserAgent(securityService.getUserAgent(hsr));
        if (isJWTAuthentication) {
            loginResponse.setJwt(jwtTokenUtil.generateToken(principal));
        }
        hsr1.getOutputStream().println(new ObjectMapper().writeValueAsString(loginResponse));
    }
}
