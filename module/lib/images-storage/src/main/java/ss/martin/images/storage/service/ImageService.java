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

    /**
     * Save image to disk.
     * @param data data.
     * @return filename.
     */
    public String saveImageToDisk(final byte[] data) {
        return ((ThrowingSupplier<String>) () -> {
            final var randomName = generateRandomFilename();
            final var file = new File(getRootFolder(), randomName);
            Files.write(Paths.get(file.toURI()), data, StandardOpenOption.CREATE_NEW);
            return randomName;
        }).get();
    }

    @Override
    public byte[] readImageFromDisk(final EntityImage image) {
        return ((ThrowingSupplier<byte[]>) () -> {
            final var file = new File(getRootFolder(), image.getFileNameOnDisk());
            return Files.readAllBytes(Paths.get(file.toURI()));
        }).get();
    }
    
    /**
     * Delete image from disk.
     * @param image image.
     */
    public void deleteImageFromDisk(final EntityImage image) {
        final var file = new File(getRootFolder(), image.getFileNameOnDisk());
        if (file.exists()) {
            file.delete();
        }
    }
    
    private File getRootFolder() {
        final var folder = new File(storageConfiguration.path());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
    
    private String generateRandomFilename() {
        return System.currentTimeMillis() + "." + UUID.randomUUID().toString();
    }
}
