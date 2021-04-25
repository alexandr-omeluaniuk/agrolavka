/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NotNull
    @Column(name = "buy_price")
    private Double buyPrice;
    /** Article number. */
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
    /** Description. */
    @FormField
    @Lob
    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;
    /** Code. */
    @Size(max = 255)
    @Column(name = "code", length = 255)
    private String code;
    /** URL. */
    @NotNull
    @Size(max = 255)
    @Column(name = "url", length = 255, unique = true)
    private String url;
    /** Quantity. */
    @Column(name = "quantity")
    private Double quantity;
    /** Updated. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date updated;
    /** SEO title. */
    @FormField
    @Size(max = 255)
    @Column(name = "seo_title", length = 255)
    private String seoTitle;
    /** SEO description. */
    @FormField
    @Size(max = 255)
    @Column(name = "seo_description", length = 255)
    private String seoDescription;
    /** Discount. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_id")
    private Discount discount;
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
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return the quantity
     */
    public Double getQuantity() {
        return quantity;
    }
    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    /**
     * @return the updated
     */
    public Date getUpdated() {
        return updated;
    }
    /**
     * @param updated the updated to set
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    /**
     * @return the seoTitle
     */
    public String getSeoTitle() {
        return seoTitle;
    }
    /**
     * @param seoTitle the seoTitle to set
     */
    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }
    /**
     * @return the seoDescription
     */
    public String getSeoDescription() {
        return seoDescription;
    }
    /**
     * @param seoDescription the seoDescription to set
     */
    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }
    /**
     * @return the discount
     */
    public Discount getDiscount() {
        return discount;
    }
    /**
     * @param discount the discount to set
     */
    public void setDiscount(Discount discount) {
        this.discount = discount;
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
//        JSONObject buyPriceJSON = new JSONObject();
//        buyPriceJSON.put("value", getBuyPrice() * 100);
//        json.put("buyPrice", buyPriceJSON);
        json.put("productFolder", getGroup().toMySkladJSONAsReference());
        json.put("description", getDescription() == null ? "" : getDescription());
        return json;
    }
}
