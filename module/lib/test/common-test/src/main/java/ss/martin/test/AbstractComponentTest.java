package ss.martin.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Common test class for testing separate Spring components.
 * @author alex
 */
@ActiveProfiles("test")
@SpringBootTest(classes = TestSpringBootApplication.class)
public abstract class AbstractComponentTest {
    
}
