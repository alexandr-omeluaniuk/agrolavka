package ss.martin.security.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.configuration.custom.LoginResponse;
import ss.martin.security.configuration.external.NavigationConfiguration;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.security.model.LoginRequest;
import ss.martin.test.AbstractMvcTest;

public abstract class PlatformSecurityMvcTest extends AbstractMvcTest {
    
    @Autowired
    private NavigationConfiguration navigationConfiguration;
    
    @Autowired
    protected CoreDao coreDao;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    protected String jwt(final StandardRole role) {
        final var response = callPost(
            navigationConfiguration.login(), 
            userRegistration(role), 
            LoginResponse.class, 
            HttpStatus.OK
        );
        return response.jwt();
    }
    
    private LoginRequest userRegistration(final StandardRole role) {
        final var email = "test.security@fake.test";
        final var password = "verySimplePassword";
        final var subscriptionEntity = DataFactory.generateSubscription(email);
        final var subscription = coreDao.create(subscriptionEntity);
        final var user = DataFactory.generateSystemUser(email, "August Rokwood");
        user.setSubscription(subscription);
        user.setPassword(passwordEncoder.encode(password));
        user.setStandardRole(role);
        user.setStatus(SystemUserStatus.ACTIVE);
        SecurityContext.executeBehalfUser(user, () -> {
            coreDao.create(user);
        });
        return new LoginRequest(email, password);
    }
    
}
