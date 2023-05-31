package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Discount.
 * @author alex
 */
@Entity
@Table(name = "discount")
public class Discount extends ExternalEntity {
    /** Discount name. */
    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    /** Products. */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "discount")
    private List<Product> products;
    /** Discount. */
    @NotNull
    @Column(name = "discount", nullable = false)
    private Double discount;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public Double getDiscount() {
        return discount;
    }
    
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Discount)) {
            return false;
        }
        Discount other = (Discount) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.agrolavka.Discount[ id=" + getId() + " ]";
    }
}
