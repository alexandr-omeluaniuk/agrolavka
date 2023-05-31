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
 * Site slide.
 * @author alex
 */
@Getter
@Setter
@Entity
@Table(name = "slide")
public class Slide extends EntityAudit {
    /** Name. */
    @NotNull
    @Size(max = 255)
    @Updatable
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    /** Title. */
    @NotNull
    @Size(max = 1000)
    @Updatable
    @Column(name = "title", length = 1000, nullable = false)
    private String title;
    /** Subtitle. */
    @Size(max = 3000)
    @Updatable
    @Column(name = "subtitle", length = 3000)
    private String subtitle;
    /** Button text. */
    @Size(max = 255)
    @Updatable
    @Column(name = "button_text", length = 255)
    private String buttonText;
    /** Button link. */
    @Size(max = 1000)
    @Updatable
    @Column(name = "button_link", length = 1000)
    private String buttonLink;
    /** Images. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "slide_images",
            joinColumns = @JoinColumn(name = "slide_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<EntityImage> images;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductsGroup)) {
            return false;
        }
        Slide other = (Slide) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "Slide[ id=" + getId() + ", name=" + getName() + " ]";
    }
}
