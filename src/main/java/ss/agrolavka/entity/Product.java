/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    /** Name. */
    @Column(name = "name", length = 1000, nullable = false)
    private String name;
    /** Price. */
    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;
    /** Images. */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<ProductImage> images;
    /** Product group. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private ProductsGroup group;
    // ============================================== SET & GET =======================================================
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
     * @return the group
     */
    public ProductsGroup getGroup() {
        return group;
    }
    /**
     * @param group the group to set
     */
    public void setGroup(ProductsGroup group) {
        this.group = group;
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
        if ((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "ss.agrolavka.model.Product[ id=" + getId() + ", name=" + getName() + ", price=" + getPrice()
                + ", group=" + (this.getGroup() != null ? this.getGroup().getName() : "") + " ]";
    }
}
