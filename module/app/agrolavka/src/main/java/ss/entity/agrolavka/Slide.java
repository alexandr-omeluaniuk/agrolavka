/*
 * Copyright (C) 2022 alex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ss.entity.agrolavka;

import java.io.Serializable;
import java.util.List;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ss.entity.martin.EntityAudit;
import ss.entity.martin.EntityImage;
import ss.martin.core.anno.Updatable;

/**
 * Site slide.
 * @author alex
 */
@Entity
@Table(name = "slide")
public class Slide extends EntityAudit implements Serializable {
    /** Default UID. */
    private static final long serialVersionUID = 1L;
    // ============================================== FIELDS ==========================================================
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
    // =============================================== SET & GET ======================================================
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }
    /**
     * @param subtitle the subtitle to set
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    /**
     * @return the buttonText
     */
    public String getButtonText() {
        return buttonText;
    }
    /**
     * @param buttonText the buttonText to set
     */
    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
    /**
     * @return the buttonLink
     */
    public String getButtonLink() {
        return buttonLink;
    }
    /**
     * @param buttonLink the buttonLink to set
     */
    public void setButtonLink(String buttonLink) {
        this.buttonLink = buttonLink;
    }
    /**
     * @return the images
     */
    public List<EntityImage> getImages() {
        return images;
    }
    /**
     * @param images the images to set
     */
    public void setImages(List<EntityImage> images) {
        this.images = images;
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
