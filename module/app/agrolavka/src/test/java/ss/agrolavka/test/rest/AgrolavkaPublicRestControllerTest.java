package ss.agrolavka.test.rest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import ss.agrolavka.test.common.AgrolavkaDataFactory;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.martin.security.test.DataFactory;


public class AgrolavkaPublicRestControllerTest extends AbstractAgrolavkaMvcTest {
    
    @Test
    public void testSearch() {
        DataFactory.silentAuthentication(coreDao);
        final var productGroup = coreDao.create(AgrolavkaDataFactory.generateProductGroup("New group"));
        coreDao.create(AgrolavkaDataFactory.generateProduct(productGroup, "Hummer", 100.0, 2.0));
        
        final var response = callGet(
            SiteUrls.URL_PUBLIC + "/search?searchText=", 
            ProductsSearchResponse.class, 
            HttpStatus.OK
        );
        assertNotNull(response);
        assertEquals(1, response.count());
        assertEquals(1, response.data().size());

        final var response2 = callGet(
            SiteUrls.URL_PUBLIC + "/search?searchText=D", 
            ProductsSearchResponse.class, 
            HttpStatus.OK
        );
        assertNotNull(response2);
        assertEquals(0, response2.count());
        assertEquals(0, response2.data().size());

        final var response3 = callGet(
            SiteUrls.URL_PUBLIC + "/search?searchText=mm", 
            ProductsSearchResponse.class, 
            HttpStatus.OK
        );
        assertNotNull(response3);
        assertEquals(1, response3.count());
        assertEquals(1, response3.data().size());
    }
}
