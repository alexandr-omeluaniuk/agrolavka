package ss.martin.images.storage.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.entity.images.storage.EntityImage;
import ss.martin.base.lang.ThrowingSupplier;
import ss.martin.images.storage.api.ImagesStorageApi;
import ss.martin.images.storage.configuration.external.StorageConfiguration;

/**
 * Image service implementation.
 * @author alex
 */
@Service
public class ImageService implements ImagesStorageApi {
    /** Platform configuration. */
    @Autowired
    private StorageConfiguration storageConfiguration;

    public String saveImageToDisk(byte[] data) throws Exception {
        String randomName = generateRandomFilename();
        File file = new File(getRootFolder(), randomName);
        Files.write(Paths.get(file.toURI()), data, StandardOpenOption.CREATE_NEW);
        return randomName;
    }

    public byte[] readImageFromDisk(EntityImage image) {
        return ((ThrowingSupplier<byte[]>) () -> {
            File file = new File(getRootFolder(), image.getFileNameOnDisk());
            return Files.readAllBytes(Paths.get(file.toURI()));
        }).get();
    }
    
    public void deleteImageFromDisk(EntityImage image) throws Exception {
        File file = new File(getRootFolder(), image.getFileNameOnDisk());
        if (file.exists()) {
            file.delete();
        }
    }
    
    private File getRootFolder() {
        File folder = new File(storageConfiguration.path());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
    
    private String generateRandomFilename() {
        return System.currentTimeMillis() + "." + UUID.randomUUID().toString();
    }
}
