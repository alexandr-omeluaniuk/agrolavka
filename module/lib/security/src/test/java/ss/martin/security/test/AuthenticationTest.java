package ss.martin.security.test;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.configuration.external.NavigationConfiguration;
import ss.martin.security.constants.SystemUserStatus;
import ss.martin.security.context.SecurityContext;
import ss.martin.test.AbstractMvcTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import ss.martin.security.configuration.custom.LoginResponse;
import ss.martin.security.constants.LoginFaultCode;
import ss.martin.security.model.LoginRequest;
import ss.martin.security.model.RestResponse;

public class AuthenticationTest extends AbstractMvcTest {
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private CoreDao coreDao;
    
    @Autowired
    private NavigationConfiguration navigationConfiguration;
    
    static Stream<Arguments> loginRequests() {
        return Stream.of(
            Arguments.of(
                Named.of(
                    "Success login", 
                    new LoginTestCase(
                        "adminSuccess@subscription.test", 
                        "12345", 
                        new LoginRequest("adminSuccess@subscription.test", "12345"),
                        null
                    )
                )
            ),
            Arguments.of(
                Named.of(
                    "Wrong password", 
                    new LoginTestCase(
                        "adminWrongPassword@subscription.test", 
                        "12345", 
                        new LoginRequest("adminWrongPassword@subscription.test", "123456"),
                        LoginFaultCode.BAD_CREDENTIALS
                    )
                )
            ),
            Arguments.of(
                Named.of(
                    "Wrong username", 
                    new LoginTestCase(
                        "adminWrongUsername@subscription.test", 
                        "12345", 
                        new LoginRequest("adminWrongUsername2@subscription.test", "12345"),
                        LoginFaultCode.WRONG_USERNAME
                    )
                )
            ),
            Arguments.of(
                Named.of(
                    "Expired subscription", 
                    new LoginTestCase(
                        "adminExpiredSubscription@subscription.test", 
                        "abc123Pass", 
                        new LoginRequest("adminExpiredSubscription@subscription.test", "abc123Pass"),
                        LoginFaultCode.SUBSCRIPTION_EXPIRED
                    )
                )
            ),
            Arguments.of(
                Named.of(
                    "Deactivated user", 
                    new LoginTestCase(
                        "adminDeactivated@subscription.test", 
                        "uPwttr7876", 
                        new LoginRequest("adminDeactivated@subscription.test", "uPwttr7876"),
                        LoginFaultCode.DEACTIVATED
                    )
                )
            )
        );
    }
    
    @ParameterizedTest
    @MethodSource("loginRequests")
    public void testLogin(final LoginTestCase testCase) {
        userRegistration(testCase);
        if (testCase.faultCode() == null) {
            final var response = callPost(navigationConfiguration.login(), testCase.request(), LoginResponse.class, HttpStatus.OK);
            assertFalse(response.jwt().isBlank());
            assertFalse(response.message().isBlank());
        } else {
            final var response = callPost(navigationConfiguration.login(), testCase.request(), RestResponse.class, HttpStatus.UNAUTHORIZED);
            assertEquals(testCase.faultCode().getCode(), response.code());
            assertEquals(testCase.faultCode().getMessage(), response.message());
        }
    }
    
    private void userRegistration(final LoginTestCase testCase) {
        final var subscriptionEntity = DataFactory.generateSubscription(testCase.registrationEmail());
        if (testCase.faultCode() == LoginFaultCode.SUBSCRIPTION_EXPIRED) {
            subscriptionEntity.setExpirationDate(new Date(System.currentTimeMillis() - 1));
        }
        final var subscription = coreDao.create(subscriptionEntity);
        final var user = DataFactory.generateSystemUser(testCase.registrationEmail(), "Sam Little");
        user.setSubscription(subscription);
        user.setPassword(passwordEncoder.encode(testCase.registrationPassword()));
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR);
        user.setStatus(testCase.faultCode() == LoginFaultCode.DEACTIVATED ? SystemUserStatus.INACTIVE : SystemUserStatus.ACTIVE);
        SecurityContext.executeBehalfUser(user, () -> {
            coreDao.create(user);
        });
    }
    
    private static record LoginTestCase(
        String registrationEmail,
        String registrationPassword,
        LoginRequest request,
        LoginFaultCode faultCode
    ) {}
}
