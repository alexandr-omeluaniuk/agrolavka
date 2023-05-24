package ss.martin.platform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.martin.platform.service.SecurityService;
import ss.martin.platform.wrapper.UserPermissions;

/**
 * Security REST controller.
 * @author ss
 */
@RestController
@RequestMapping("/api/platform/security")
public class PermissionsRestController {
    /** Security service. */
    @Autowired
    private SecurityService securityService;
    
    /**
     * Get user permissions.
     * @return user permissions.
     * @throws Exception error.
     */
    @RequestMapping(value = "/permissions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserPermissions getUserPermissions() throws Exception {
        return securityService.getUserPermissions();
    }
}
