package ss.entity.images.storage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    // ============================================= FIELDS ===========================================================
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
    // ============================================= SET & GET ========================================================
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the size
     */
    public Long getSize() {
        return size;
    }
    /**
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }
    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }
    /**
     * @return the fileNameOnDisk
     */
    public String getFileNameOnDisk() {
        return fileNameOnDisk;
    }
    /**
     * @param fileNameOnDisk the fileNameOnDisk to set
     */
    public void setFileNameOnDisk(String fileNameOnDisk) {
        this.fileNameOnDisk = fileNameOnDisk;
    }
    // ================================================================================================================
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
}
