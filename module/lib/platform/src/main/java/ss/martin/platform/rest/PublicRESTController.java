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
package ss.martin.platform.rest;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.martin.security.dao.UserDao;
import ss.martin.security.model.RestResponse;
import ss.martin.security.api.RegistrationUserService;

/**
 * Public resources.
 * @author ss
 */
@RestController
@RequestMapping("/api/platform/public")
public class PublicRESTController {
    /** System user service. */
    @Autowired
    private RegistrationUserService systemUserService;
    /** User DAO. */
    @Autowired
    private UserDao userDAO;
    /**
     * Finish registration.
     * @param params parameters.
     * @return empty response.
     * @throws Exception error.
     */
    @RequestMapping(value = "/finish-registration", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse finishRegistration(@RequestBody Map<String, String> params) throws Exception {
        systemUserService.finishRegistration(params.get("validation"), params.get("password"));
        return new RestResponse();
    }
    /**
     * Check validation string.
     * @param validationString validation string.
     * @return user if validation string is exist or empty response if no.
     * @throws Exception error.
     */
    @RequestMapping(value = "/check-validation-string/{validationString}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object checkValidationString(@PathVariable("validationString") String validationString)
            throws Exception {
        final var user = userDAO.findByValidationString(validationString);
        return user.isEmpty() ? new RestResponse() : user.get();
    }
}