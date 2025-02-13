package ss.entity.agrolavka;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ss.entity.images.storage.EntityImage;
import ss.entity.security.EntityAudit;

import java.util.List;

/**
 * Site slide.
 * @author alex
 */
@Entity
@Table(name = "slide")
public class Slide extends EntityAudit implements EntityWithImages, Comparable<Slide> {
    /** Name. */
    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    /** Title. */
    @NotNull
    @Size(max = 1000)
    @Column(name = "title", length = 1000, nullable = false)
    private String title;
    /** Subtitle. */
    @Size(max = 3000)
    @Column(name = "subtitle", length = 3000)
    private String subtitle;
    /** Button text. */
    @Size(max = 255)
    @Column(name = "button_text", length = 255)
    private String buttonText;
    /** Button link. */
    @Size(max = 1000)
    @Column(name = "button_link", length = 1000)
    private String buttonLink;
    @Lob
    @Size(max = 65535)
    @Column(name = "html_content", length = 65535)
    private String htmlContent;
    @Column(name = "slide_order")
    private Integer order;
    /** Images. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "slide_images",
            joinColumns = @JoinColumn(name = "slide_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<EntityImage> images;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSubtitle() {
        return subtitle;
    }
    
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    
    public String getButtonText() {
        return buttonText;
    }
    
    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
    
    public String getButtonLink() {
        return buttonLink;
    }
    
    public void setButtonLink(String buttonLink) {
        this.buttonLink = buttonLink;
    }
    
    @Override
    public List<EntityImage> getImages() {
        return images;
    }
    
    @Override
    public void setImages(List<EntityImage> images) {
        this.images = images;
    }


    /** Slide order. */
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    
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

    @Override
    public int compareTo(Slide slide) {
        final var order1 = slide.getOrder() == null ? 0 : slide.getOrder();
        final var order2 = this.getOrder() == null ? 0 : this.getOrder();
        if (order1 == order2) {
            return 0;
        } else if (order1 > order2) {
            return -1;
        } else {
            return 1;
        }
    }
}
