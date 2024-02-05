package ss.agrolavka.test.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;

public class MySkladServiceTest extends AbstractAgrolavkaMvcTest {
    
    static {
        System.setProperty("mysklad.api.url", "http://localhost:20233/mysklad/");
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
        
        final var productVariants = mySkladService.getProductVariants();
        
        Assertions.assertEquals(5, productVariants.size());
        productVariants.forEach(variant -> {
            Assertions.assertTrue(names.contains(variant.getName()));
        });
    }
}
