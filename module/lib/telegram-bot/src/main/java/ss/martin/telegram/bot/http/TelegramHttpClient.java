package ss.martin.telegram.bot.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
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

    public TelegramHttpClient(final String rootUri) {
        this.rootUri = rootUri;
        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(20)).build();
        this.objectMapper = new ObjectMapper();
    }

    public <T> T get(final String endpoint, Class<T> responseType) {
        final var response = get(endpoint);
        if (response.ok()) {
            return ((ThrowingSupplier<T>) () -> objectMapper.readValue(response.result().toString(), responseType)).get();
        } else {
            throw new TelegramBotException(response.result().toString());
        }
    }

    private Response get(final String endpoint) {
        return ((ThrowingSupplier<Response>) () -> objectMapper.readValue(
                httpClient.send(
                    HttpRequest.newBuilder().uri(new URI(rootUri + endpoint))
                        .header("Content-Type", "application/json").GET().build(),
                    BodyHandlers.ofString()).body(),
                Response.class
            )).get();
    }

}
