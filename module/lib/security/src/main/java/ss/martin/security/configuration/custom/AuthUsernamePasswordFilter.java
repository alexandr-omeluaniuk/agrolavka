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
import java.io.BufferedReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Custom username/password filter.
 * @author Alexandr Omeluaniuk
 */
public class AuthUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AuthUsernamePasswordFilter.class);
    /** Username. */
    private String jsonUsername;
    /** Password. */
    private String jsonPassword;
    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String password;
        if (accept != null && accept.contains("application/json")) {
            password = this.jsonPassword;
        } else {
            password = super.obtainPassword(request);
        }
        return password;
    }
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String username;
        if (accept != null && accept.contains("application/json")) {
            username = this.jsonUsername;
        } else {
            username = super.obtainUsername(request);
        }
        return username;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                //json transformation
                ObjectMapper mapper = new ObjectMapper();
                LoginRequest loginRequest = mapper.readValue(sb.toString(), LoginRequest.class);
                this.jsonUsername = loginRequest.getUsername();
                this.jsonPassword = loginRequest.getPassword();
            } catch (Exception e) {
                LOG.warn("attempt authentication error: ", e);
            }
        }
        return super.attemptAuthentication(request, response);
    }
}
