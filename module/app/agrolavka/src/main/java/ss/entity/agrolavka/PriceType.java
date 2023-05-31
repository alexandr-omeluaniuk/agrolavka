package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Price type.
 * @author alex
 */
@Getter
@Setter
@Entity
@Table(name = "price_type")
public class PriceType extends ExternalEntity {
    /** Price type name. */
    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PriceType)) {
            return false;
        }
        PriceType other = (PriceType) object;
        return !((this.getId() == null && other.getId() != null)
            || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "PriceType[ id=" + getId() + " ]";
    }
    
    public JSONObject toMySkladJSON() {
        JSONObject json = new JSONObject();
        JSONObject meta = new JSONObject();
        meta.put("href", "https://online.moysklad.ru/api/remap/1.2/context/companysettings/pricetype/"
                + getExternalId());
        meta.put("type", "pricetype");
        meta.put("mediaType", "application/json");
        json.put("meta", meta);
        return json;
    }
}
