package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ss.entity.security.EntityAudit;

import java.util.List;

@Entity
@Table(name = "product_attribute_item")
public class ProductAttributeItem extends EntityAudit {

    @NotNull
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_attribute_id")
    private ProductAttribute productAttribute;

    @JsonIgnore
    @OneToMany(mappedBy = "attributeItem", fetch = FetchType.LAZY)
    private List<ProductAttributeLink> links;

    @NotNull
    @Column(name = "url", length = 255, nullable = false)
    private String url;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductAttribute getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ProductAttribute productAttribute) {
        this.productAttribute = productAttribute;
    }

    public List<ProductAttributeLink> getLinks() {
        return links;
    }

    public void setLinks(List<ProductAttributeLink> links) {
        this.links = links;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductAttributeItem other)) {
            return false;
        }
        return !((this.getId() == null && other.getId() != null)
            || (this.getId() != null && !this.getId().equals(other.getId())));
    }

    @Override
    public String toString() {
        return "ss.entity.agrolavka.ProductAttributeItem[ id=" + getId() + " ]";
    }
}
