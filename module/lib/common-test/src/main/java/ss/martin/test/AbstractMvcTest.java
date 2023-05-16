package ss.martin.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = TestSpringBootApplication.class)
@AutoConfigureMockMvc
public abstract class AbstractMvcTest {
    
    @Autowired
    protected MockMvc mockMvc;
}
