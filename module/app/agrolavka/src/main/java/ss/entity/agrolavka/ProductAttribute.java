package ss.entity.agrolavka;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import ss.entity.martin.DataModel;

import java.util.List;

@Entity
@Table(name = "product_attribute")
public class ProductAttribute extends DataModel {

    @NotNull
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "productAttribute", cascade = CascadeType.ALL)
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
