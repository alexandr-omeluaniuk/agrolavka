package ss.martin.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ss.entity.martin.DataModel;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;
import ss.martin.security.api.RegistrationUserService;
import ss.martin.security.constants.PlatformUrl;
import ss.martin.security.model.UserPermissions;
import ss.martin.security.service.SecurityService;

@RestController
@RequestMapping(PlatformUrl.SECURITY_URL)
public class SecurityRestController {
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
    public EntitySearchResponse<SystemUser> searchUsers(
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
    public SystemUser getUser(@PathVariable("id") Long id) {
        return coreDao.findById(id, SystemUser.class);
    }

    @PostMapping("/users")
    public DataModel createUser(@RequestBody SystemUser user) {
        return systemUserService.createSubscriptionUser(user);
    }

    @PutMapping("/users")
    public SystemUser updateUser(@RequestBody SystemUser user) {
        final var fromDb = coreDao.findById(user.getId(), SystemUser.class);
        fromDb.setEmail(user.getEmail());
        fromDb.setFirstname(user.getFirstname());
        fromDb.setLastname(user.getLastname());
        return coreDao.update(fromDb);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        final var fromDb = coreDao.findById(id, SystemUser.class);
        fromDb.setActive(false);
        coreDao.update(fromDb);
    }

    @GetMapping("/subscriptions")
    public EntitySearchResponse<Subscription> searchSubscriptions(
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
        return coreDao.searchEntities(request, Subscription.class);
    }

    @GetMapping("/subscriptions/{id}")
    public Subscription getSubscription(@PathVariable("id") Long id) {
        return coreDao.findById(id, Subscription.class);
    }

    @PostMapping("/subscriptions")
    public Subscription createSubscription(@RequestBody Subscription subscription) {
        return systemUserService.createSubscriptionAndAdmin(subscription);
    }

    @PutMapping("/subscriptions")
    public Subscription updateSubscription(@RequestBody Subscription subscription) {
        final var fromDb = coreDao.findById(subscription.getId(), Subscription.class);
        fromDb.setOrganizationName(subscription.getOrganizationName());
        fromDb.setStarted(subscription.getStarted());
        fromDb.setExpirationDate(subscription.getExpirationDate());
        fromDb.setSubscriptionAdminEmail(subscription.getSubscriptionAdminEmail());
        fromDb.setActive(subscription.isActive());
        return coreDao.update(fromDb);
    }

    @DeleteMapping("/subscriptions/{id}")
    public void deleteSubscription(@PathVariable("id") Long id) {
        final var fromDb = coreDao.findById(id, Subscription.class);
        fromDb.setActive(false);
        coreDao.update(fromDb);
    }
}
