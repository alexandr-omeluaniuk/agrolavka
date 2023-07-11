package ss.entity.agrolavka;

import java.util.List;
import ss.entity.images.storage.EntityImage;

/**
 * Entity with images.
 * @author alex
 */
public interface EntityWithImages {
    
    /**
     * Get images.
     * @return images.
     */
    List<EntityImage> getImages();
    
    /**
     * Set images.
     * @param images images. 
     */
    void setImages(List<EntityImage> images);
}
