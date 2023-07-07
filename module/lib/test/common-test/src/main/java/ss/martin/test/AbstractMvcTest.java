package ss.martin.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = TestSpringBootApplication.class)
@AutoConfigureMockMvc
public abstract class AbstractMvcTest {
    
    private static final String USER_AGENT = "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0";
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private String jwt;
    
    private HttpHeaders specificHeaders;
    
    protected <T, R> R callPost(final String url, final T requestBody, final Class<R> returnType, final HttpStatus status) {
        return assertDoesNotThrow(() -> {
            final var payload = requestBody == null ? new byte[0] : objectMapper.writeValueAsBytes(requestBody);
            final var response = mockMvc.perform(
                    post(url).content(payload)
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
    
    protected <T, R> R callMultipart(final String url, final MockMultipartFile[] files, final Class<R> returnType, final HttpStatus status) {
        return assertDoesNotThrow(() -> {
            final MockMultipartHttpServletRequestBuilder builder = multipart(url);
            Stream.of(files).forEach(file -> builder.file(file));
            final var headers = requestHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
            final var response = mockMvc.perform(builder.headers(headers))
                .andDo(print()).andExpect(status().is(status.value())).andReturn();
            final var content = response.getResponse().getContentAsByteArray();
            if (content.length > 0) {
                return objectMapper.readValue(content, returnType);
            } else {
                return null;
            }
        });
    }
    
    protected <T, R> R callPut(final String url, final T requestBody, final Class<R> returnType, final HttpStatus status) {
        return assertDoesNotThrow(() -> {
            final var payload = requestBody == null ? new byte[0] : objectMapper.writeValueAsBytes(requestBody);
            final var response = mockMvc.perform(
                    put(url).content(payload)
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
    
    protected <T, R> R callDelete(final String url, final Class<R> returnType, final HttpStatus status) {
        return assertDoesNotThrow(() -> {
            final var response = mockMvc.perform(
                    delete(url)
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
    
    protected void withHeaders(final HttpHeaders headers, final Runnable runnable) {
        this.specificHeaders = headers;
        runnable.run();
        this.specificHeaders = null;
    }
    
    private HttpHeaders requestHeaders() {
        final var headers = Optional.ofNullable(specificHeaders).orElse(new HttpHeaders());
        headers.add(HttpHeaders.USER_AGENT, USER_AGENT);
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }
        if (!headers.containsKey(HttpHeaders.ACCEPT)) {
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        }
        if (jwt != null) {
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        }
        return headers;
    }
    
}
