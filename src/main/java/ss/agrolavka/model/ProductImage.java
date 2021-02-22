/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Product image.
 * @author alex
 */
@Entity
@Table(name = "product_image")
public class ProductImage extends ExternalEntity implements Serializable {
    /** UID. */
    private static final long serialVersionUID = 1L;
    // ============================================= FIEDLS ===========================================================
    /** ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** Product. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    /** File name. */
    @Column(name = "filename", length = 10000)
    private String filename;
    /** Size. */
    @Column(name = "image_size")
    private Long imageSize;
    /** Data. */
    @NotNull
    @Lob
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;
    // ============================================= SET & GET ========================================================
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
     * @return the product
     */
    public Product getProduct() {
        return product;
    }
    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }
    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    /**
     * @return the imageData
     */
    public byte[] getImageData() {
        return imageData;
    }
    /**
     * @param imageData the imageData to set
     */
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    /**
     * @return the imageSize
     */
    public Long getImageSize() {
        return imageSize;
    }
    /**
     * @param imageSize the imageSize to set
     */
    public void setImageSize(Long imageSize) {
        this.imageSize = imageSize;
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
        if (!(object instanceof ProductImage)) {
            return false;
        }
        ProductImage other = (ProductImage) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "ss.agrolavka.model.ProductImage[ id=" + getId() + " ]";
    }
}
