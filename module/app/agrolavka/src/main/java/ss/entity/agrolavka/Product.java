package ss.entity.agrolavka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ss.agrolavka.wrapper.ProductVolume;
import ss.entity.images.storage.EntityImage;
import ss.martin.core.anno.Updatable;

/**
 * Product.
 * @author alex
 */
@Entity
@Table(name = "product")
public class Product extends ExternalEntity {
    /** Name. */
    @Updatable
    @Column(name = "name", length = 1000, nullable = false)
    private String name;
    /** Price. */
    @Updatable
    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;
    /** Min price. */
    @Column(name = "min_price")
    private Double minPrice;
    /** Max price. */
    @Column(name = "max_price")
    private Double maxPrice;
    /** Buy price. */
    @NotNull
    @Column(name = "buy_price")
    private Double buyPrice;
    /** Article number. */
    @Size(max = 255)
    @Column(name = "article", length = 255)
    private String article;
    /** Images. */
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "product_images",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<EntityImage> images;
    /** Product group. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private ProductsGroup group;
    /** Description. */
    @Updatable
    @Lob
    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;
    /** Description in HTML format. */
    @Updatable
    @Lob
    @Size(max = 65535)
    @Column(name = "html_description", length = 65535)
    private String htmlDescription;
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
    @Updatable
    @Size(max = 255)
    @Column(name = "seo_title", length = 255)
    private String seoTitle;
    /** SEO description. */
    @Updatable
    @Size(max = 255)
    @Column(name = "seo_description", length = 255)
    private String seoDescription;
    /** Discount. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_id")
    private Discount discount;
    /** Product is hidden. */
    @Column(name = "hidden")
    private boolean hidden;
    /** Product volumes. */
    @Size(max = 3000)
    @Column(name = "volumes", length = 3000)
    private String volumes;
    /** Video URL. */
    @Updatable
    @Size(max = 3000)
    @Column(name = "video_url", length = 3000)
    private String videoURL;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public ProductsGroup getGroup() {
        return group;
    }
    
    public void setGroup(ProductsGroup group) {
        this.group = group;
    }
    
    public List<EntityImage> getImages() {
        return images;
    }
    
    public void setImages(List<EntityImage> images) {
        this.images = images;
    }
    
    public Double getBuyPrice() {
        return buyPrice;
    }
    
    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }
    
    public String getArticle() {
        return article;
    }
    
    public void setArticle(String article) {
        this.article = article;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    
    public Date getUpdated() {
        return updated;
    }
    
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public String getSeoTitle() {
        return seoTitle;
    }
    
    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }
    
    public String getSeoDescription() {
        return seoDescription;
    }
    
    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }
    
    public Discount getDiscount() {
        return discount;
    }
    
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
    
    public String getHtmlDescription() {
        return htmlDescription;
    }
    
    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    
    public String getVolumes() {
        return volumes;
    }
    
    public void setVolumes(String volumes) {
        this.volumes = volumes;
    }
    
    public Double getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    
    public Double getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public String getVideoURL() {
        return videoURL;
    }
    
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        return !((this.getId() == null && other.getId() != null)
            || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "Product[ id=" + getId() + ", name=" + getName() + ", price=" + getPrice()
                + ", group=" + (this.getGroup() != null ? this.getGroup().getName() : "")
                + ", article=" + (getArticle() == null ? "" : getArticle()) + " ]";
    }
    
    public Double getDiscountPrice() {
        if (this.getDiscount() != null) {
            return this.getPrice() - (this.getPrice() * this.getDiscount().getDiscount() / 100);
        } else {
            return this.getPrice();
        }
    }
    
    public List<ProductVolume> getProductVolumes() throws JsonProcessingException {
        var list = getVolumes() == null ? new ProductVolume[0] : new ObjectMapper().readValue(getVolumes(), ProductVolume[].class);
        return Arrays.asList(list);
    }
}
