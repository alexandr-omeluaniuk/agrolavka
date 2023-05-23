package ss.martin.images.storage.test;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.images.storage.configuration.external.StorageConfiguration;
import ss.martin.test.AbstractComponentTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.context.SecurityContextHolder;
import ss.entity.images.storage.EntityImage;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.context.SecurityContext;

public class ImageServiceTest extends AbstractComponentTest {
    
    @Autowired
    private CoreDao coreDao;
    
    @Autowired
    private StorageConfiguration storageConfiguration;
    
    @BeforeEach
    public void beforeTest() {
        DataFactory.silentAuthentication(coreDao);
    }
    
    @Test
    public void testSaveImage() throws IOException {
        final var entity = coreDao.create(getEntity());
        
        assertNotNull(entity.getId());
        assertNotNull(entity.getSize());
        assertNotNull(entity.getFileNameOnDisk());
        assertEquals(0, entity.getData().length);
        final var file = new File(new File(storageConfiguration.path()), entity.getFileNameOnDisk());
        assertTrue(file.exists());
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
