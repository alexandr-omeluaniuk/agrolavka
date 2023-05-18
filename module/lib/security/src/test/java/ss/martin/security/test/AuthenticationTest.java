package ss.martin.security.test;

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
                    new LoginTestCase<>(
                        "admin@subscription.test", 
                        "12345", 
                        new LoginRequest("admin@subscription.test", "12345"),
                        HttpStatus.OK,
                        LoginResponse.class,
                        (response) -> {
                            assertNotNull(response.jwt());
                            assertFalse(response.jwt().isBlank());
                            assertNotNull(response.message());
                        }
                    )
                )
            ),
            Arguments.of(
                Named.of(
                    "Wrong password", 
                    new LoginTestCase<>(
                        "admin@subscription.test", 
                        "12345", 
                        new LoginRequest("admin@subscription.test", "123456"),
                        HttpStatus.UNAUTHORIZED,
                        RestResponse.class,
                        (response) -> {
                            assertEquals(LoginFaultCode.BAD_CREDENTIALS.getCode(), response.code());
                            assertEquals(LoginFaultCode.BAD_CREDENTIALS.getMessage(), response.message());
                        }
                    )
                )
            ),
            Arguments.of(
                Named.of(
                    "Wrong username", 
                    new LoginTestCase<>(
                        "admin@subscription.test", 
                        "12345", 
                        new LoginRequest("admin2@subscription.test", "12345"),
                        HttpStatus.UNAUTHORIZED,
                        RestResponse.class,
                        (response) -> {
                            assertEquals(LoginFaultCode.WRONG_USERNAME.getCode(), response.code());
                            assertEquals(LoginFaultCode.WRONG_USERNAME.getMessage(), response.message());
                        }
                    )
                )
            )
        );
    }
    
    @ParameterizedTest
    @MethodSource("loginRequests")
    public <T, R> void testLogin(final LoginTestCase<T, R> testCase) {
        userRegistration(testCase.registrationEmail(), testCase.registrationPassword());
        
        final var response = callPost(navigationConfiguration.login(), testCase.request(), testCase.responseType(), testCase.statusCode());

        testCase.verificationFunction().accept(response);
    }
    
    private void userRegistration(final String email, final String password) {
        final var subscription = coreDao.create(DataFactory.generateSubscription(email));
        final var user = DataFactory.generateSystemUser(email, "Sam Little");
        user.setSubscription(subscription);
        user.setPassword(passwordEncoder.encode(password));
        user.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR);
        user.setStatus(SystemUserStatus.ACTIVE);
        SecurityContext.executeBehalfUser(user, () -> {
            coreDao.create(user);
        });
    }
    
    private static record LoginTestCase<T, R>(
        String registrationEmail,
        String registrationPassword,
        T request,
        HttpStatus statusCode,
        Class<R> responseType,
        Consumer<R> verificationFunction
    ) {}
}
