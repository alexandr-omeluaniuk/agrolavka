package ss.martin.images.storage.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.images.storage.configuration.external.StorageConfiguration;
import ss.martin.test.AbstractComponentTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import ss.entity.images.storage.EntityImage;
import ss.martin.base.exception.PlatformException;
import ss.martin.core.dao.CoreDao;
import ss.martin.images.storage.service.ImageService;

public class ImageServiceTest extends AbstractComponentTest {
    
    @Autowired
    private CoreDao coreDao;
    
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private StorageConfiguration storageConfiguration;
    
    @BeforeEach
    public void beforeTest() throws IOException {
        FileUtils.forceDelete(new File(storageConfiguration.path()));
        DataFactory.silentAuthentication(coreDao);
    }
    
    @Test
    public void testEntityImage() throws IOException {
        final var entity = coreDao.create(getEntity());
        
        assertNotNull(entity.getId());
        assertNotNull(entity.getSize());
        assertNotNull(entity.getFileNameOnDisk());
        assertEquals(0, entity.getData().length);
        final var file = new File(new File(storageConfiguration.path()), entity.getFileNameOnDisk());
        assertTrue(file.exists());
        
        final var newName = "new name.jpg";
        entity.setData(new byte[] { 1, 2, 3, 4, 5 });
        entity.setName(newName);
        final var updatedEntity = coreDao.update(entity);
        
        assertEquals(newName, updatedEntity.getName());
        final byte[] data = imageService.readImageFromDisk(updatedEntity);
        assertEquals(5, data.length);
        
        coreDao.delete(updatedEntity.getId(), EntityImage.class);
        
        final var exception = assertThrows(PlatformException.class, () -> imageService.readImageFromDisk(updatedEntity));
        assertTrue(exception.getCause() instanceof NoSuchFileException);
        
        assertDoesNotThrow(() -> imageService.deleteImageFromDisk(updatedEntity));
    }
    
    private EntityImage getEntity() throws IOException {
        final var entity = new EntityImage();
        entity.setData(data());
        entity.setName("wallpaperflare.com_wallpaper.jpg");
        entity.setType("image/jpg");
        entity.setSize(Long.valueOf(String.valueOf(entity.getData().length)));
        return entity;
    }
    
    private byte[] data() throws IOException {
        try (final var is = getClass().getResourceAsStream("/wallpaperflare.com_wallpaper.jpg")) {
            final var buff = new byte[is.available()];
            is.read(buff);
            return buff;
        }
    }
}
