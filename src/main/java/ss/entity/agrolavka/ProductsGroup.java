/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.json.JSONObject;

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
