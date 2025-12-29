package ss.agrolavka.test.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ss.agrolavka.task.EuropostLocationScanner;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import ss.entity.agrolavka.EuropostLocation;
import ss.martin.security.api.AlertService;

import static org.mockito.Mockito.*;

@Disabled
public class EuropostLocationScannerTest extends AbstractAgrolavkaMvcTest {
    
    @Autowired
    private EuropostLocationScanner task;
    
    @MockBean
    private AlertService alertService;
    
    @Test
    public void testRunTask() {
        task.runTask();
        verify(alertService, never()).sendAlert(any(), any());
        
        Assertions.assertFalse(coreDao.getAll(EuropostLocation.class).isEmpty());
    }
}
