package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.json.JSONObject;

/**
 * Product variant.
 * @author alex
 */
@Entity
@Table(name = "product_variant")
public class ProductVariant extends ExternalEntity implements Comparable<ProductVariant> {
    
    @Column(name = "name", length = 1000, nullable = false)
    private String name;
    
    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;
    
    @NotNull
    @Column(name = "parent_id", length = 255, nullable = false)
    private String parentId;

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
     * @return the externalProductId
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductVariant)) {
            return false;
        }
        ProductVariant other = (ProductVariant) object;
        return !((this.getId() == null && other.getId() != null)
            || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        final var json = new JSONObject();
        json.put("name", getName());
        json.put("price", getPrice());
        return json.toString();
    }

    @Override
    public int compareTo(ProductVariant o) {
        if (o.getPrice() < getPrice()) {
            return 1;
        } else if (o.getPrice() > getPrice()) {
            return -1;
        } else {
            return 0;
        }
    }
}
