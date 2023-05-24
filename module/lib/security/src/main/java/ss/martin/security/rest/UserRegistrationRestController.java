package ss.martin.security.rest;

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
import ss.martin.security.constants.PlatformUrl;

/**
 * Public resources.
 * @author ss
 */
@RestController
@RequestMapping(PlatformUrl.PUBLIC_RESOURCES_URL)
public class UserRegistrationRestController {
    /** System user service. */
    @Autowired
    private RegistrationUserService systemUserService;
    /** User DAO. */
    @Autowired
    private UserDao userDao;
    
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
        final var user = userDao.findByValidationString(validationString);
        return user.isEmpty() ? new RestResponse() : user.get();
    }
}
