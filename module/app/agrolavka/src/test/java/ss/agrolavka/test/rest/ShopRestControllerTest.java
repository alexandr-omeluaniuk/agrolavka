package ss.agrolavka.test.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import ss.agrolavka.test.common.AgrolavkaDataFactory;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ss.agrolavka.constants.SiteConstants;
import ss.entity.agrolavka.Shop;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.model.EntitySearchResponse;

public class ShopRestControllerTest extends AbstractAgrolavkaMvcTest {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testCreate_WithoutFiles() throws JsonProcessingException {
        final var shop = AgrolavkaDataFactory.generateShop();
        final var shopJson = new MockMultipartFile(
            "entity", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(shop)
        );
        withAuthorization(jwt(StandardRole.ROLE_SUBSCRIPTION_USER), () -> {
            final var newShop = callMultipart(HttpMethod.POST, SiteConstants.URL_PROTECTED + "/shop", new MockMultipartFile[] {
                shopJson
            }, Shop.class, HttpStatus.OK);
            shopAsserts(newShop);
            assertTrue(newShop.getImages().isEmpty());
        });
    }
    
    @Test
    public void testCreate_WithFiles() throws JsonProcessingException, IOException {
        final var shop = AgrolavkaDataFactory.generateShop();
        final var shopJson = new MockMultipartFile(
            "entity", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(shop)
        );
        final var image1 = new MockMultipartFile("image", "fileX1.jpg", MediaType.IMAGE_JPEG_VALUE, data());
        final var image2 = new MockMultipartFile("image", "fileX2.jpg", MediaType.IMAGE_JPEG_VALUE, data());
        final var image3 = new MockMultipartFile("image", "fileX3.jpg", MediaType.IMAGE_JPEG_VALUE, data());
        withAuthorization(jwt(StandardRole.ROLE_SUBSCRIPTION_USER), () -> {
            final var newShop = callMultipart(
                HttpMethod.POST, 
                SiteConstants.URL_PROTECTED + "/shop", 
                new MockMultipartFile[] {
                    shopJson, image1, image2
                }, 
                Shop.class, 
                HttpStatus.OK
            );
            shopAsserts(newShop);
            assertEquals(2, newShop.getImages().size());
            
            // get by id
            final var getShop = callGet(SiteConstants.URL_PROTECTED + "/shop/" + newShop.getId(), Shop.class, HttpStatus.OK);
            shopAsserts(getShop);
            assertEquals(2, getShop.getImages().size());
            
            // update
            final var newAddress = "New address";
            final var newDescription = "New description";
            getShop.setAddress(newAddress);
            getShop.setDescription(newDescription);
            final var imageForRemoval = getShop.getImages().get(0);
            getShop.getImages().remove(imageForRemoval);
            final var shopJsonUpdate = new MockMultipartFile(
                "entity", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(getShop)
            );
            final var updatedShop = callMultipart(
                HttpMethod.PUT, 
                SiteConstants.URL_PROTECTED + "/shop", 
                new MockMultipartFile[] {
                    shopJsonUpdate, image3
                }, 
                Shop.class, 
                HttpStatus.OK
            );
            assertNotNull(updatedShop);
            final var getShopAfterUpdate = callGet(SiteConstants.URL_PROTECTED + "/shop/" + newShop.getId(), Shop.class, HttpStatus.OK);
            assertEquals(newAddress, getShopAfterUpdate.getAddress());
            assertEquals(newDescription, getShopAfterUpdate.getDescription());
            assertEquals(2, getShopAfterUpdate.getImages().size());
            assertEquals("fileX2.jpg", getShopAfterUpdate.getImages().get(0).getName());
            assertEquals("fileX3.jpg", getShopAfterUpdate.getImages().get(1).getName());
            
            // get list
            final var returnType = new TypeReference<EntitySearchResponse<Shop>>() { };
            final var shops = callGet(SiteConstants.URL_PROTECTED + "/shop", returnType, HttpStatus.OK);
            assertTrue(shops.total() > 0);
            assertFalse(shops.data().isEmpty());
            shopAsserts(shops.data().get(0));
            
            //delete
            callDelete(SiteConstants.URL_PROTECTED + "/shop/" + newShop.getId(), Void.class, HttpStatus.OK);
            assertNull(coreDao.findById(newShop.getId(), Shop.class));
        });
    }
    
    private void shopAsserts(final Shop shop) {
        assertNotNull(shop);
        assertTrue(shop.getId() > 0);
        assertNotNull(shop.getAddress());
        assertNotNull(shop.getDescription());
        assertNotNull(shop.getLatitude());
        assertNotNull(shop.getLongitude());
        assertNotNull(shop.getPhone());
        assertNotNull(shop.getTitle());
        assertNotNull(shop.getWorkingHours());
    }
    
    private byte[] data() throws IOException {
        try (final var is = getClass().getResourceAsStream("/wallpaperflare.com_wallpaper.jpg")) {
            final var buff = new byte[is.available()];
            is.read(buff);
            return buff;
        }
    }
}
