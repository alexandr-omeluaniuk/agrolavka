/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.json.JSONArray;
import org.json.JSONObject;
import ss.entity.martin.EntityImage;
import ss.martin.platform.anno.security.FormField;

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
    @FormField
    @Column(name = "name", length = 1000, nullable = false)
    private String name;
    /** Price. */
    @FormField
    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;
    /** Buy price. */
    @FormField
    @NotNull
    @Column(name = "buy_price")
    private Double buyPrice;
    /** Article number. */
    @FormField
    @Size(max = 255)
    @Column(name = "article", length = 255)
    private String article;
    /** Images. */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "product_images",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    private List<EntityImage> images;
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
    /**
     * @return the images
     */
    @JsonIgnore
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
     * @return the buyPrice
     */
    public Double getBuyPrice() {
        return buyPrice;
    }
    /**
     * @param buyPrice the buyPrice to set
     */
    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }
    /**
     * @return the article
     */
    public String getArticle() {
        return article;
    }
    /**
     * @param article the article to set
     */
    public void setArticle(String article) {
        this.article = article;
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
        return "Product[ id=" + getId() + ", name=" + getName() + ", price=" + getPrice()
                + ", group=" + (this.getGroup() != null ? this.getGroup().getName() : "")
                + ", article=" + (getArticle() == null ? "" : getArticle()) + " ]";
    }
    
    public JSONObject toMySkladJSON(PriceType priceType) {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("article", getArticle());
        JSONArray salePrices = new JSONArray();
        JSONObject productPrice = new JSONObject();
        productPrice.put("value", Double.valueOf(getPrice() * 100).intValue());
        productPrice.put("priceType", priceType.toMySkladJSON());
        salePrices.put(productPrice);
        json.put("salePrices", salePrices);
        JSONObject buyPriceJSON = new JSONObject();
        buyPriceJSON.put("value", getBuyPrice() * 100);
        json.put("buyPrice", buyPriceJSON);
        json.put("productFolder", getGroup().toMySkladJSON());
        return json;
    }
}
