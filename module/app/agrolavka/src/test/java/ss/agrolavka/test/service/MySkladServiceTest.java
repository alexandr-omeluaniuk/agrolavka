package ss.agrolavka.test.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;

import java.util.Arrays;

public class MySkladServiceTest extends AbstractAgrolavkaMvcTest {
    
    static {
        WireMockServer wireMockServer = new WireMockServer(20233);
        wireMockServer.start();
    }
    
    @Autowired
    private MySkladIntegrationService mySkladService;
    
    @Test
    public void testGetModifications() {
        final var names = Arrays.asList(
            "Товар (синий)", 
            "Товар (красный)", 
            "Сетка тканая(затеняющая) 35%, 2*50м (2*50)",
            "Сетка тканая(затеняющая) 55%, 10*50м (10*50)",
            "Сетка тканая(затеняющая) 55%, 2*50м (10*50)"
        );
        final var parentIds = Arrays.asList(
            "160536e0-78f5-11eb-0a80-00de00183884", 
            "70f8e3cc-e7c7-11ec-0a80-0c2300061277",
            "f44579cf-7122-11eb-0a80-08d30028b6e4",
            "dc00e2dd-7122-11eb-0a80-08d30028932b"
        );
        
        final var productVariants = mySkladService.getProductVariants();
        
        Assertions.assertEquals(5, productVariants.size());
        productVariants.forEach(variant -> {
            Assertions.assertTrue(names.contains(variant.getName()));
            Assertions.assertTrue(parentIds.contains(variant.getParentId()));
            Assertions.assertNotNull(variant.getPrice());
            Assertions.assertNotNull(variant.getExternalId());
        });
    }
}
