package ss.agrolavka.test.common;

import org.springframework.boot.test.context.SpringBootTest;
import ss.martin.security.test.PlatformSecurityMvcTest;

@SpringBootTest(classes = AgrolavkaTestApplication.class)
public abstract class AbstractAgrolavkaMvcTest extends PlatformSecurityMvcTest {
    
}
