package ss.martin.platform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ss.entity.martin.DataModel;
import ss.entity.security.SystemUser;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;
import ss.martin.platform.service.SecurityService;
import ss.martin.platform.wrapper.UserPermissions;
import ss.martin.security.api.RegistrationUserService;
import ss.martin.security.constants.PlatformUrl;

@RestController
@RequestMapping(PlatformUrl.SECURITY_URL)
public class PermissionsRestController {
    /** Security service. */
    @Autowired
    private SecurityService securityService;

    @Autowired
    private RegistrationUserService systemUserService;

    @Autowired
    private CoreDao coreDao;

    @GetMapping("/permissions")
    public UserPermissions getUserPermissions() throws Exception {
        return securityService.getUserPermissions();
    }

    @GetMapping("/users")
    public EntitySearchResponse<SystemUser> search(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "order_by", required = false) String orderBy
    ) {
        final var request = new EntitySearchRequest(
                page == null ? 1 : page,
                pageSize == null ? Integer.MAX_VALUE : pageSize,
                order,
                orderBy
        );
        return coreDao.searchEntities(request, SystemUser.class);
    }

    @GetMapping("/users/{id}")
    public SystemUser get(@PathVariable("id") Long id) {
        return coreDao.findById(id, SystemUser.class);
    }

    @PostMapping("/users")
    public DataModel create(@RequestBody SystemUser user) {
        return systemUserService.createSubscriptionUser(user);
    }

    @PutMapping("/users")
    public SystemUser update(@RequestBody SystemUser user) {
        final var fromDb = coreDao.findById(user.getId(), SystemUser.class);
        fromDb.setEmail(user.getEmail());
        fromDb.setFirstname(user.getFirstname());
        fromDb.setLastname(user.getLastname());
        return coreDao.update(fromDb);
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable("id") Long id) {
        final var fromDb = coreDao.findById(id, SystemUser.class);
        fromDb.setActive(false);
        coreDao.update(fromDb);
    }
}
