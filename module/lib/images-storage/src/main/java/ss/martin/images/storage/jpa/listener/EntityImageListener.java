package ss.martin.images.storage.jpa.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import ss.entity.images.storage.EntityImage;
import ss.martin.images.storage.service.ImageService;

/**
 * Entity image listener.
 * @author alex
 */
public class EntityImageListener {
    /** Image service. */
    private ImageService imageService;
    
    @Autowired
    public void init(final ImageService imageService) {
        this.imageService = imageService;
    }
    
    @PrePersist
    protected void prePersist(EntityImage entity) throws Exception {
        entity.setFileNameOnDisk(imageService.saveImageToDisk(entity.getData()));
        entity.setData(new byte[0]);  // release space in DB
    }
    
    @PreUpdate
    protected void preUpdate(EntityImage entity) throws Exception {
        if (entity.getData() != null && entity.getData().length > 0) {
            imageService.deleteImageFromDisk(entity);
            entity.setFileNameOnDisk(imageService.saveImageToDisk(entity.getData()));
        }
        entity.setData(new byte[0]);  // release space in DB
    }
    
    @PreRemove
    protected void preRemove(EntityImage entity) throws Exception {
        imageService.deleteImageFromDisk(entity);
    }
}
