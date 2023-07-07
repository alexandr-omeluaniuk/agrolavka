package ss.entity.images.storage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import ss.entity.security.EntityAudit;
import ss.martin.core.anno.Updatable;
import ss.martin.images.storage.deserializer.ByteArrayDeserializer;
import ss.martin.images.storage.jpa.listener.EntityImageListener;

/**
 * Entity image.
 * @author alex
 */
@Entity
@Table(name = "entity_image")
@EntityListeners(EntityImageListener.class)
public class EntityImage extends EntityAudit {
    /** Image file name. */
    @Updatable
    @NotNull
    @Size(max = 1000)
    @Column(name = "name", length = 1000, nullable = false)
    private String name;
    /** Image mime type. */
    @Updatable
    @NotNull
    @Size(max = 255)
    @Column(name = "type", length = 255, nullable = false)
    private String type;
    /** Image size, in bytes. */
    @Updatable
    @NotNull
    @Column(name = "image_size", nullable = false)
    private Long size;
    /** Absolute path on disk. */
    @NotNull
    @Column(name = "file_name_on_disk", length = 255, nullable = false)
    private String fileNameOnDisk;
    /** Data. */
    @JsonDeserialize(using = ByteArrayDeserializer.class)
    @Updatable
    @Column(name = "image_data", nullable = true)
    private byte[] data;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
    
    public String getFileNameOnDisk() {
        return fileNameOnDisk;
    }
    
    public void setFileNameOnDisk(String fileNameOnDisk) {
        this.fileNameOnDisk = fileNameOnDisk;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EntityImage)) {
            return false;
        }
        EntityImage other = (EntityImage) object;
        if ((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "ss.entity.martin.EntityImage[ id=" + getId() + " ]";
    }
    
    public static List<EntityImage> getActualImages(
            final List<EntityImage> imagesDB,
            final List<EntityImage> images
    ) {
        final var map = imagesDB.stream().collect(Collectors.toMap(EntityImage::getId, Function.identity()));
        final var actualImages = new ArrayList<EntityImage>();
        images.forEach(image -> {
            if (image.getData() != null) {
                actualImages.add(image);
            } else if (image.getId() != null && map.containsKey(image.getId())) {
                actualImages.add(map.get(image.getId()));
            }
        });
        return actualImages;
    }
}
