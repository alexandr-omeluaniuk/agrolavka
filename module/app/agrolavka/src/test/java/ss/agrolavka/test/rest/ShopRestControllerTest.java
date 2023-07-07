package ss.agrolavka.test.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import ss.agrolavka.test.common.AgrolavkaDataFactory;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ss.agrolavka.constants.SiteConstants;
import ss.entity.agrolavka.Shop;
import ss.martin.core.constants.StandardRole;

public class ShopRestControllerTest extends AbstractAgrolavkaMvcTest {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testCreate_WithoutFiles() throws JsonProcessingException {
        final var shop = AgrolavkaDataFactory.generateShop();
        final var shopJson = new MockMultipartFile(
            "shop", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(shop)
        );
        withAuthorization(jwt(StandardRole.ROLE_SUBSCRIPTION_USER), () -> {
            final var newShop = callMultipart(SiteConstants.URL_PROTECTED + "/shop", new MockMultipartFile[] {
                shopJson
            }, Shop.class, HttpStatus.OK);
            assertNotNull(newShop);
            assertTrue(newShop.getId() > 0);
            assertNotNull(newShop.getAddress());
            assertNotNull(newShop.getDescription());
            assertNotNull(newShop.getLatitude());
            assertNotNull(newShop.getLongitude());
            assertNotNull(newShop.getPhone());
            assertNotNull(newShop.getTitle());
            assertNotNull(newShop.getWorkingHours());
            assertNull(newShop.getImages());
        });
    }
    
    @Test
    public void testCreate_WithFiles() throws JsonProcessingException, IOException {
        final var shop = AgrolavkaDataFactory.generateShop();
        final var shopJson = new MockMultipartFile(
            "shop", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(shop)
        );
        final var image1 = new MockMultipartFile("image", "fileX1.jpg", MediaType.IMAGE_JPEG_VALUE, data());
        final var image2 = new MockMultipartFile("image", "fileX2.jpg", MediaType.IMAGE_JPEG_VALUE, data());
        withAuthorization(jwt(StandardRole.ROLE_SUBSCRIPTION_USER), () -> {
            final var newShop = callMultipart(SiteConstants.URL_PROTECTED + "/shop", new MockMultipartFile[] {
                shopJson, image1, image2
            }, Shop.class, HttpStatus.OK);
            assertNotNull(newShop);
            assertTrue(newShop.getId() > 0);
            assertNotNull(newShop.getAddress());
            assertNotNull(newShop.getDescription());
            assertNotNull(newShop.getLatitude());
            assertNotNull(newShop.getLongitude());
            assertNotNull(newShop.getPhone());
            assertNotNull(newShop.getTitle());
            assertNotNull(newShop.getWorkingHours());
            assertEquals(2, newShop.getImages().size());
        });
    }
    
    private byte[] data() throws IOException {
        try (final var is = getClass().getResourceAsStream("/wallpaperflare.com_wallpaper.jpg")) {
            final var buff = new byte[is.available()];
            is.read(buff);
            return buff;
        }
    }
}
