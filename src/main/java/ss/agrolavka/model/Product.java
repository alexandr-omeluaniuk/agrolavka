/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Product.
 * @author alex
 */
@Entity
@Table(name = "product")
public class Product extends ExternalEntity implements Serializable {
    /** Default UID. */
    private static final long serialVersionUID = 1L;
    // ============================================== FIELDS ==========================================================
    /** Id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** Name. */
    @Column(name = "name", length = 1000, nullable = false)
    private String name;
    /** Product group ID. */
    @Column(name = "product_groupd_id", length = 255)
    private String productGroupId;
    /** Price. */
    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;
    /** Images. */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductImage> images;
    /** Has images. */
    @Column(name = "has_images", nullable = false)
    private boolean hasImages;
    // ============================================== SET & GET =======================================================
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
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
     * @return the productGroupId
     */
    public String getProductGroupId() {
        return productGroupId;
    }
    /**
     * @param productGroupId the productGroupId to set
     */
    public void setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
    }
    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }
    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }
    /**
     * @return the images
     */
    public List<ProductImage> getImages() {
        return images;
    }
    /**
     * @param images the images to set
     */
    public void setImages(List<ProductImage> images) {
        this.images = images;
    }
    /**
     * @return the hasImages
     */
    public boolean isHasImages() {
        return hasImages;
    }
    /**
     * @param hasImages the hasImages to set
     */
    public void setHasImages(boolean hasImages) {
        this.hasImages = hasImages;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "ss.agrolavka.model.Product[ id=" + getId() + " ]";
    }
}
