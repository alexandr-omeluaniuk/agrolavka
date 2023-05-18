package ss.martin.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    
    private String jwt;
    
    protected <T, R> R callPost(final String url, final T requestBody, final Class<R> returnType, final HttpStatus status) {
        return assertDoesNotThrow(() -> {
            final var response = mockMvc.perform(
                    post(url).content(objectMapper.writeValueAsBytes(requestBody))
                    .headers(requestHeaders())
            ).andDo(print()).andExpect(status().is(status.value())).andReturn();
            final var content = response.getResponse().getContentAsByteArray();
            if (content.length > 0) {
                return objectMapper.readValue(content, returnType);
            } else {
                return null;
            }
        });
    }
    
    protected <T, R> R callGet(final String url, final Class<R> returnType, final HttpStatus status) {
        return assertDoesNotThrow(() -> {
            final var response = mockMvc.perform(
                    get(url)
                    .headers(requestHeaders())
            ).andDo(print()).andExpect(status().is(status.value())).andReturn();
            final var content = response.getResponse().getContentAsByteArray();
            if (content.length > 0) {
                return objectMapper.readValue(content, returnType);
            } else {
                return null;
            }
        });
    }
    
    protected void withAuthorization(final String jwtToken, final Runnable runnable) {
        this.jwt = jwtToken;
        runnable.run();
        this.jwt = null;
    }
    
    private HttpHeaders requestHeaders() {
        final var headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, USER_AGENT);
        if (jwt != null) {
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        }
        return headers;
    }
    
}
