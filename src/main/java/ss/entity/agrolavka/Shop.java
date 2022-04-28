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

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ss.entity.martin.EntityAudit;
import ss.entity.martin.EntityImage;
import ss.martin.platform.anno.security.FormField;

/**
 * Shop
 * @author alex
 */
@Entity
@Table(name = "shop")
public class Shop extends EntityAudit {
    // ========================================== FIELDS ==============================================================
    /** Shop title. */
    @FormField
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title", length = 255)
    private String title;
    /** Shop description. */
    @FormField
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "description", length = 5000)
    private String description;
    /** Main image. */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "main_image_id")
    private EntityImage mainImage;
    /** Images. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "shop_images",
            joinColumns = @JoinColumn(name = "shop_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<EntityImage> images;
    /** Latitude. */
    @FormField
    @NotNull
    @Column(name = "latitude")
    private Double latitude;
    /** Longitude. */
    @FormField
    @NotNull
    @Column(name = "longitude")
    private Double longitude;
    /** Address. */
    @FormField
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address")
    private String address;
    /** Working hours. */
    @FormField
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "working_hours")
    private String workingHours;
    // ========================================== SET & GET ===========================================================
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the mainImage
     */
    public EntityImage getMainImage() {
        return mainImage;
    }
    /**
     * @param mainImage the mainImage to set
     */
    public void setMainImage(EntityImage mainImage) {
        this.mainImage = mainImage;
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
    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }
    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }
    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return the workingHours
     */
    public String getWorkingHours() {
        return workingHours;
    }
    /**
     * @param workingHours the workingHours to set
     */
    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
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