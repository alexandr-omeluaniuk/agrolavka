package ss.entity.agrolavka;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ss.entity.images.storage.EntityImage;
import ss.entity.security.EntityAudit;
import ss.martin.core.anno.Updatable;

/**
 * Shop
 * @author alex
 */
@Getter
@Setter
@Entity
@Table(name = "shop")
public class Shop extends EntityAudit {
    /** Shop title. */
    @Updatable
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title", length = 255)
    private String title;
    /** Shop description. */
    @Updatable
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "description", length = 5000)
    private String description;
    /** Images. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "shop_images",
            joinColumns = @JoinColumn(name = "shop_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<EntityImage> images;
    /** Latitude. */
    @Updatable
    @NotNull
    @Column(name = "latitude")
    private Double latitude;
    /** Longitude. */
    @Updatable
    @NotNull
    @Column(name = "longitude")
    private Double longitude;
    /** Address. */
    @Updatable
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address")
    private String address;
    /** Working hours. */
    @Updatable
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "working_hours")
    private String workingHours;
    /** Phone */
    @Updatable
    @Size(max = 255)
    @Column(name = "phone")
    private String phone;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Shop)) {
            return false;
        }
        Shop other = (Shop) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.agrolavka.Shop[ id=" + getId() + " ]";
    }
}
