package ss.agrolavka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ss.agrolavka.dao.ProductDAO;
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
import java.util.ArrayList;
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

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Value("${ikassa.client:0asid-iasd-iasd}")
    private String clientId;

    @Value("${ikassa.secret:7sdfdsfk8dfjlsdfj}")
    private String secretId;

    @Value("${ikassa.unp:1234567890}")
    private Integer unp;

    @Autowired
    private ProductDAO productDAO;

    private static IKassaAuthResponse accessToken = null;

    @PostConstruct
    private void init() {
        final var list = new ArrayList<String>();
        list.add("f2af42f5-7122-11eb-0a80-08d30028b447");
        list.add("de7b5704-7122-11eb-0a80-08d300289681");
        final var products = productDAO.getByExternalIds(list);
        createProducts(products);
    }

    public void createProducts(List<Product> products) {
        final var payload = products.stream().map(product -> {
            return new iKassaProduct(
                product.getArticle(),
                product.getCode(),
                product.getId().toString(),
                false,
                product.getName(),
                // price with discounts????
                BigDecimal.valueOf(product.getPrice() * 100).intValue(),
                true,
                "product"
            );
        }).toList();
        final ThrowingSupplier<String> execFun = () -> {
            final var payloadStr = objectMapper.writeValueAsString(new CreateIKassaProductsRequest(payload));
            System.out.println(payloadStr);
            final var resp = httpClient.send(
                HttpRequest.newBuilder().uri(new URI("https://wms.ikassa.by/api/wms.sku.create"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", accessToken.accessToken()).POST(
                        HttpRequest.BodyPublishers.ofString(payloadStr)
                    ).build(),
                HttpResponse.BodyHandlers.ofString());
            return resp.body();
        };
        final var response = executeWithAuthentication(execFun);
        System.out.println(response);
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
