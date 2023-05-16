package ss.martin.security.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.configuration.external.NavigationConfiguration;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.test.AbstractMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpHeaders;
import ss.martin.security.model.LoginRequest;

public class AuthenticationTest extends AbstractMvcTest {
    
    private static final String USERNAME = "admin@subscription.test";
    private static final String PASSWORD = UUID.randomUUID().toString();
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private CoreDao coreDao;
    
    @Autowired
    private NavigationConfiguration navigationConfiguration;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testLogin() {
        userRegistration();
        assertDoesNotThrow(() -> {
            mockMvc.perform(
                    post(navigationConfiguration.login())
                        .content(objectMapper.writeValueAsBytes(new LoginRequest(USERNAME, PASSWORD)))
                    .header(HttpHeaders.USER_AGENT, "Android 12")
            ).andDo(print()).andExpect(status().isOk());
        });
    }
    
    private void userRegistration() {
        final var subscriptionAdminEmail = "admin@subscription.test";
        final var subscription = coreDao.create(DataFactory.generateSubscription(subscriptionAdminEmail));
        final var user = DataFactory.generateSystemUser(subscriptionAdminEmail, "Sam Little");
        user.setSubscription(subscription);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR);
        user.setStatus(SystemUserStatus.ACTIVE);
        SecurityContext.executeBehalfUser(user, () -> {
            coreDao.create(user);
        });
    }
}
