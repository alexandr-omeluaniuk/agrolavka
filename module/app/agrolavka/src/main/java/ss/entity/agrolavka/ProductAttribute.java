package ss.entity.agrolavka;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ss.entity.security.EntityAudit;

import java.util.List;

@Entity
@Table(name = "product_attribute")
public class ProductAttribute extends EntityAudit {

    @NotNull
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Column(name = "color", length = 10, nullable = false)
    private String color;

    @NotNull
    @Column(name = "url", length = 255, nullable = false)
    private String url;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productAttribute")
    @Fetch(FetchMode.JOIN)
    private List<ProductAttributeItem> items;

    public List<ProductAttributeItem> getItems() {
        return items;
    }

    public void setItems(List<ProductAttributeItem> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
        if (!(object instanceof ProductAttribute other)) {
            return false;
        }
        return !((this.getId() == null && other.getId() != null)
            || (this.getId() != null && !this.getId().equals(other.getId())));
    }

    @Override
    public String toString() {
        return "ss.entity.agrolavka.ProductAttribute[ id=" + getId() + " ]";
    }
}
