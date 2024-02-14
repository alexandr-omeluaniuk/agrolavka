package ss.martin.telegram.bot.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import ss.martin.base.lang.ThrowingSupplier;
import ss.martin.telegram.bot.exception.TelegramBotException;
import ss.martin.telegram.bot.model.Response;

/**
 * HTTP client.
 *
 * @author alex
 */
public class TelegramHttpClient {

    private final HttpClient httpClient;

    private final String rootUri;

    private final ObjectMapper objectMapper;

    /**
     * Constructor.
     * @param rootUri root URI. 
     */
    public TelegramHttpClient(final String rootUri) {
        this.rootUri = rootUri;
        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(20)).build();
        this.objectMapper = new ObjectMapper();
    }
    
    public <T> T post(final String endpoint, Object payload, final Class<T> responseType) {
        return extractSingle(post(endpoint, payload), responseType);
    }
    
    /**
     * Post request.
     * @param endpoint endpoint.
     * @param payload payload.
     * @return response text.
     */
    public Response post(final String endpoint, Object payload) {
        return ((ThrowingSupplier<Response>) () -> objectMapper.readValue(
                httpClient.send(
                    HttpRequest.newBuilder().uri(new URI(rootUri + endpoint))
                        .header("Content-Type", "application/json").POST(
                            HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload))
                        ).build(),
                    BodyHandlers.ofString()).body(),
                Response.class
            )).get();
    }

    /**
     * Get request.
     * @param <T> type.
     * @param endpoint endpoint.
     * @param responseType response type.
     * @return response.
     */
    public <T> T get(final String endpoint, final Class<T> responseType) {
        return extractSingle(get(endpoint), responseType);
    }

    private Response get(final String endpoint) {
        final var response = ((ThrowingSupplier<Response>) () -> objectMapper.readValue(
                httpClient.send(
                    HttpRequest.newBuilder().uri(new URI(rootUri + endpoint))
                        .header("Content-Type", "application/json").GET().build(),
                    BodyHandlers.ofString()).body(),
                Response.class
            )).get();
        return response;
    }
    
    public <T> List<T> getList(final String endpoint, Class<T> responseType) {
        return extractList(get(endpoint), responseType);
    }
    
    private <T> T extractSingle(final Response response, final Class<T> responseType) {
        if (response.ok()) {
            return ((ThrowingSupplier<T>) () -> objectMapper.readValue(response.result(), responseType)).get();
        } else {
            throw new TelegramBotException(response.errorCode() + ", " + response.description());
        }
    }
    
    private <T> List<T> extractList(final Response response, final Class<T> responseType) {
        if (response.ok()) {
            final var javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, responseType);
            return ((ThrowingSupplier<List<T>>) () -> objectMapper.readValue(response.result(), javaType)).get();
        } else {
            throw new TelegramBotException(response.result());
        }
    }
}
