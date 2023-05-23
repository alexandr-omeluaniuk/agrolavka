package ss.martin.images.storage.test;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.images.storage.configuration.external.StorageConfiguration;
import ss.martin.images.storage.service.ImageService;
import ss.martin.test.AbstractComponentTest;
import static org.junit.jupiter.api.Assertions.*;

public class ImageServiceTest extends AbstractComponentTest {
    
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private StorageConfiguration storageConfiguration;
    
    @Test
    public void testSaveImageToDisk() throws IOException {
        final var fileName = imageService.saveImageToDisk(data());
        
        assertNotNull(fileName);
        final var file = new File(new File(storageConfiguration.path()), fileName);
        assertTrue(file.exists());
    }
    
    private byte[] data() throws IOException {
        try (final var is = getClass().getResourceAsStream("/wallpaperflare.com_wallpaper.jpg")) {
            final var buff = new byte[is.available()];
            is.read(buff);
            return buff;
        }
    }
}
