/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import org.json.JSONObject;
import ss.entity.images.storage.EntityImage;
import ss.martin.core.anno.Updatable;

/**
 * Products group.
 * @author alex
 */
@Entity
@Table(name = "products_group")
public class ProductsGroup extends ExternalEntity implements Serializable, Comparable<ProductsGroup> {
    /** Default UID. */
    private static final long serialVersionUID = 1L;
    // =============================================== FIELDS =========================================================
    /** Name. */
    @Updatable
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
    /** URL. */
    @NotNull
    @Size(max = 255)
    @Column(name = "url", length = 255, unique = true)
    private String url;
    /** Top category. */
    @Updatable
    @Column(name = "is_top_category")
    private Boolean topCategory;
    /** Description. */
    @Updatable
    @Size(max = 4096)
    @Column(name = "description", length = 4096)
    private String description;
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
    /** Images. */
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "product_group_images",
            joinColumns = @JoinColumn(name = "product_group_id", referencedColumnName = "id"),
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
     * @return the parentId
     */
    public String getParentId() {
        return parentId;
    }
    /**
     * @param parentId the parentId to set
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }
    /**
     * @param products the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
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
     * @return the topCategory
     */
    public Boolean isTopCategory() {
        return topCategory;
    }
    /**
     * @param topCategory the topCategory to set
     */
    public void setTopCategory(Boolean topCategory) {
        this.topCategory = topCategory;
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
        meta.put("href", "https://online.moysklad.ru/api/remap/1.2/entity/productfolder/" + getExternalId());
        meta.put("metadataHref", "https://online.moysklad.ru/api/remap/1.2/entity/productfolder/metadata");
        meta.put("type", "productfolder");
        meta.put("mediaType", "application/json");
        meta.put("uuidHref", "https://online.moysklad.ru/app/#good/edit?id=" + getExternalId());
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
