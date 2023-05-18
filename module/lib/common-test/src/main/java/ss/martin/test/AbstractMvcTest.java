package ss.martin.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestSpringBootApplication.class)
@AutoConfigureMockMvc
public abstract class AbstractMvcTest {
    
    private static final String USER_AGENT = "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0";
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    protected <T, R> R callPost(final String url, final T requestBody, final Class<R> returnType, final HttpStatus status) {
        return assertDoesNotThrow(() -> {
            final var response = mockMvc.perform(
                    post(url).content(objectMapper.writeValueAsBytes(requestBody))
                    .header(HttpHeaders.USER_AGENT, USER_AGENT)
            ).andDo(print()).andExpect(status().is(status.value())).andReturn();
            return objectMapper.readValue(response.getResponse().getContentAsByteArray(), returnType);
        });
    }
}
