package ss.martin.security.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.entity.security.SystemUser;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.api.SystemUserService;
import ss.martin.security.dao.UserDao;
import ss.martin.test.AbstractComponentTest;

public class SystemUserServiceTest extends AbstractComponentTest {
    
    @Autowired
    private SystemUserService systemUserService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CoreDao coreDao;
    
    @Test
    public void testSuperUserCheck() {
        assertNotNull(userDao.findSuperUser());
        systemUserService.superUserCheck();
        assertEquals(1, coreDao.getAll(SystemUser.class).size());
    }
}
