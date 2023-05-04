package ss.martin.platform.service;

import ss.entity.martin.EntityImage;

/**
 * Image service.
 * @author alex
 */
public interface ImageService {
    /**
     * Save image to disk.
     * @param data image data.
     * @return absolute file path.
     * @throws Exception error.
     */
    String saveImageToDisk(byte[] data) throws Exception;
    /**
     * Delete image from disk.
     * @param image entity image.
     * @throws Exception error.
     */
    void deleteImageFromDisk(EntityImage image) throws Exception;
    /**
     * Read image from disk.
     * @param image image.
     * @return image data.
     * @throws Exception error.
     */
    byte[] readImageFromDisk(EntityImage image) throws Exception;
}
