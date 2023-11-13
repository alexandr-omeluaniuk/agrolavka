package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import org.json.JSONObject;
import ss.entity.images.storage.EntityImage;

/**
 * Products group.
 * @author alex
 */
@Entity
@Table(name = "products_group")
public class ProductsGroup extends ExternalEntity implements Comparable<ProductsGroup>, EntityWithImages {
    /** Name. */
    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    /** Parent group ID. */
    @Size(max = 255)
    @Column(name = "parent_id", length = 255)
    private String parentId;
    /** Products. */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<Product> products;
    /** URL. */
    @NotNull
    @Size(max = 255)
    @Column(name = "url", length = 255, unique = true)
    private String url;
    /** Top category. */
    @Column(name = "is_top_category")
    private Boolean topCategory;
    /** Description. */
    @Size(max = 4096)
    @Column(name = "description", length = 4096)
    private String description;
    /** SEO title. */
    @Size(max = 255)
    @Column(name = "seo_title", length = 255)
    private String seoTitle;
    /** SEO description. */
    @Size(max = 255)
    @Column(name = "seo_description", length = 255)
    private String seoDescription;
    /** Images. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "product_group_images",
            joinColumns = @JoinColumn(name = "product_group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<EntityImage> images;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Boolean isTopCategory() {
        return topCategory;
    }
    
    public void setTopCategory(Boolean topCategory) {
        this.topCategory = topCategory;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    @Override
    public List<EntityImage> getImages() {
        return images;
    }
    
    @Override
    public void setImages(List<EntityImage> images) {
        this.images = images;
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
        ProductsGroup other = (ProductsGroup) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ProductsGroup[ id=" + getId() + ", name=" + getName()
                + ", externalId=" + getExternalId() + ", parentId=" + getParentId() + " ]";
    }

    @Override
    public int compareTo(ProductsGroup o) {
        return getName().compareTo(o.getName());
    }
    
    public JSONObject toMySkladJSONAsReference() {
        JSONObject json = new JSONObject();
        JSONObject meta = new JSONObject();
        meta.put("href", "https://api.moysklad.ru/api/remap/1.2/entity/productfolder/" + getExternalId());
        meta.put("metadataHref", "https://api.moysklad.ru/api/remap/1.2/entity/productfolder/metadata");
        meta.put("type", "productfolder");
        meta.put("mediaType", "application/json");
        meta.put("uuidHref", "https://api.moysklad.ru/app/#good/edit?id=" + getExternalId());
        json.put("meta", meta);
        return json;
    }
    
    public JSONObject toMySkladJSON() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("description", getDescription() == null ? "" : getDescription());
        if (getParentId() != null) {
            JSONObject meta = new JSONObject();
            meta.put("href", "https://online.moysklad.ru/api/remap/1.2/entity/productfolder/" + getParentId());
            meta.put("metadataHref", "https://online.moysklad.ru/api/remap/1.2/entity/productfolder/metadata");
            meta.put("type", "productfolder");
            meta.put("mediaType", "application/json");
            meta.put("uuidHref", "https://online.moysklad.ru/app/#good/edit?id=" + getParentId());
            JSONObject productFolder = new JSONObject();
            productFolder.put("meta", meta);
            json.put("productFolder", productFolder);
        }
        return json;
    }
}
