package ss.agrolavka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ss.agrolavka.wrapper.CreateIKassaProductsRequest;
import ss.agrolavka.wrapper.IKassaAuthResponse;
import ss.agrolavka.wrapper.iKassaProduct;
import ss.entity.agrolavka.Product;
import ss.martin.base.lang.ThrowingSupplier;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Integration with iKassa
 */
@Service
public class IKassaService {

    private static final String URL = "https://accounts.ikassa.by";

    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(20)).build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ikassa.client:0asid-iasd-iasd}")
    private String clientId;

    @Value("${ikassa.secret:7sdfdsfk8dfjlsdfj}")
    private String secretId;

    @Value("${ikassa.unp:1234567890}")
    private Integer unp;

    private static IKassaAuthResponse accessToken = null;

    public void createProducts(List<Product> products) {
        final var payload = products.stream().map(product -> {
            return new iKassaProduct(
                product.getArticle(),
                product.getCode(),
                product.getId().toString(),
                true,
                product.getName(),
                BigDecimal.valueOf(product.getPrice()).intValue(),
                true,
                product.getGroup().getName()
            );
        }).toList();
        final ThrowingSupplier<String> execFun = ((ThrowingSupplier<String>) () -> objectMapper.readValue(
            httpClient.send(
                HttpRequest.newBuilder().uri(new URI(URL + "/api/wms.sku.create"))
                    .header("Content-Type", "application/json").POST(
                        HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(new CreateIKassaProductsRequest(payload)))
                    ).build(),
                HttpResponse.BodyHandlers.ofString()).body(),
            String.class
        ));
        final var response = executeWithAuthentication(execFun);
    }

    private <T> T executeWithAuthentication(ThrowingSupplier<T> execFun) {
        if (accessToken == null || accessToken.expiresIn() >= System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3)) {
            doAuthentication();
        }
        return execFun.get();
    }

    private void doAuthentication() {
        accessToken = ((ThrowingSupplier<IKassaAuthResponse>) () -> objectMapper.readValue(
            httpClient.send(
                HttpRequest.newBuilder().uri(new URI(URL + "/oauth/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded").POST(
                        HttpRequest.BodyPublishers.ofString(
                            "client_id=" + clientId + "&client_secret=" + secretId
                                + "&grant_type=client_credentials&audience=" + unp + "&scope=api"
                        )
                    ).build(),
                HttpResponse.BodyHandlers.ofString()).body(),
            IKassaAuthResponse.class
        )).get();
    }
}
