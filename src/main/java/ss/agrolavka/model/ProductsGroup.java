/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    /** ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** Name. */
    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    /** Parent group ID. */
    @Size(max = 255)
    @Column(name = "parent_id", length = 255)
    private String parentId;
    // =============================================== SET & GET ======================================================
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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    @Override
    public String toString() {
        return "ss.agrolavka.model.ProductsGroup[ id=" + getId() + ", name=" + getName()
                + ", externalId=" + getExternalId() + ", parentId=" + getParentId() + " ]";
    }

    @Override
    public int compareTo(ProductsGroup o) {
        return getName().compareTo(o.getName());
    }
}
