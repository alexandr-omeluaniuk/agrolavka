package ss.martin.telegram.bot.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import ss.martin.base.lang.ThrowingSupplier;

/**
 * HTTP client.
 * @author alex
 */
public class TelegramHttpClient {
    
    private final HttpClient httpClient;
    
    private final String rootUri;
    
    public TelegramHttpClient(final String rootUri) {
        this.rootUri = rootUri;
        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(20)).build();
    }
    
    public String get(final String endpoint) {
        return ((ThrowingSupplier<String>) () -> 
            httpClient.send(
                HttpRequest.newBuilder().uri(new URI(rootUri + endpoint))
                    .header("Content-Type", "application/json").GET().build(), 
                BodyHandlers.ofString()).body()
            ).get();
    }
    
}
