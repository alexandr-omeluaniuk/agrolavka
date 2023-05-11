package ss.martin.security.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.security.api.SystemUserService;
import ss.martin.test.AbstractComponentTest;

public class SystemUserServiceTest extends AbstractComponentTest {
    
    @Autowired
    private SystemUserService systemUserService;
    
    @Test
    public void testSuperUserCheck() {
        
    }
}
