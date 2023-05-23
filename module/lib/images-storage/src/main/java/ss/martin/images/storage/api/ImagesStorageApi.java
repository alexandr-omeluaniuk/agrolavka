package ss.martin.images.storage.api;

import ss.entity.images.storage.EntityImage;

/**
 * Images storage API.
 * @author alex
 */
public interface ImagesStorageApi {
    
    /**
     * Read image data from storage.
     * @param image entity of image.
     * @return image data.
     */
    byte[] readImageFromDisk(EntityImage image);
}
