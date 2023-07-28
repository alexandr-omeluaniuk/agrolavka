package ss.agrolavka.test.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import ss.entity.agrolavka.EntityWithImages;
import ss.entity.martin.DataModel;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.model.EntitySearchResponse;

public abstract class AbstractEntityWithImagesRestControllerTest<T extends DataModel & EntityWithImages> 
    extends AbstractAgrolavkaMvcTest {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    protected abstract Class<T> entityClass();
    
    protected abstract TypeReference<EntitySearchResponse<T>> listType();
    
    protected abstract T generate();
    
    protected abstract String endpoint();
    
    protected abstract T modifyEntityBeforeUpdate(T entity);
    
    protected abstract void verifyUpdatedEntity(T updatedEntity);
    
    protected abstract void entityAsserts(T entity);
    
    @Test
    public void testCreateWithoutImages() throws JsonProcessingException {
        final var entity = generate();
        final var entityJson = new MockMultipartFile(
            "entity", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(entity)
        );
        withAuthorization(jwt(StandardRole.ROLE_SUBSCRIPTION_USER), () -> {
            final var newEntity = callMultipart(HttpMethod.POST, SiteUrls.URL_PROTECTED + endpoint(), new MockMultipartFile[] {
                entityJson
            }, entityClass(), HttpStatus.OK);
            entityAsserts(newEntity);
            assertTrue(newEntity.getImages().isEmpty());
        });
    }
    
    @Test
    public void testCrudOperations() throws JsonProcessingException, IOException {
        final var entity = generate();
        final var entityJson = new MockMultipartFile(
            "entity", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(entity)
        );
        final var image1 = new MockMultipartFile("image", "fileX1.jpg", MediaType.IMAGE_JPEG_VALUE, data());
        final var image2 = new MockMultipartFile("image", "fileX2.jpg", MediaType.IMAGE_JPEG_VALUE, data());
        final var image3 = new MockMultipartFile("image", "fileX3.jpg", MediaType.IMAGE_JPEG_VALUE, data());
        withAuthorization(jwt(StandardRole.ROLE_SUBSCRIPTION_USER), () -> {
            final var newEntity = callMultipart(
                HttpMethod.POST, 
                SiteUrls.URL_PROTECTED + endpoint(), 
                new MockMultipartFile[] {
                    entityJson, image1, image2
                }, 
                entityClass(), 
                HttpStatus.OK
            );
            entityAsserts(newEntity);
            assertEquals(2, newEntity.getImages().size());
            
            // get by id
            final var getEntity = callGet(
                SiteUrls.URL_PROTECTED + endpoint() + "/" + newEntity.getId(), 
                entityClass(), 
                HttpStatus.OK
            );
            entityAsserts(getEntity);
            assertEquals(2, getEntity.getImages().size());
            
            // update
            modifyEntityBeforeUpdate(getEntity);
            final var imageForRemoval = getEntity.getImages().get(0);
            getEntity.getImages().remove(imageForRemoval);
            final var entityJsonUpdate = new MockMultipartFile(
                "entity", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(getEntity)
            );
            final var updatedEntity = callMultipart(
                HttpMethod.PUT, 
                SiteUrls.URL_PROTECTED + endpoint(), 
                new MockMultipartFile[] {
                    entityJsonUpdate, image3
                }, 
                entityClass(), 
                HttpStatus.OK
            );
            assertNotNull(updatedEntity);
            final var getEntityAfterUpdate = callGet(
                SiteUrls.URL_PROTECTED + endpoint() + "/" + newEntity.getId(), 
                entityClass(), 
                HttpStatus.OK
            );
            verifyUpdatedEntity(getEntityAfterUpdate);
            assertEquals(2, getEntityAfterUpdate.getImages().size());
            assertEquals("fileX2.jpg", getEntityAfterUpdate.getImages().get(0).getName());
            assertEquals("fileX3.jpg", getEntityAfterUpdate.getImages().get(1).getName());
            
            // get list
            final var entitiesList = callGet(SiteUrls.URL_PROTECTED + endpoint(), listType(), HttpStatus.OK);
            assertTrue(entitiesList.total() > 0);
            assertFalse(entitiesList.data().isEmpty());
            entityAsserts(entitiesList.data().get(0));
            
            //delete
            callDelete(SiteUrls.URL_PROTECTED + endpoint() + "/" + newEntity.getId(), Void.class, HttpStatus.OK);
            assertNull(coreDao.findById(newEntity.getId(), entityClass()));
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
