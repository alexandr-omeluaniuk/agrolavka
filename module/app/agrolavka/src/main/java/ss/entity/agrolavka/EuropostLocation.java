package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ss.entity.martin.DataModel;

/**
 * Europost location.
 * @author alex
 */
@Getter
@Setter
@Entity
@Table(name = "europost_location")
public class EuropostLocation extends DataModel implements Comparable<EuropostLocation> {
    @JsonProperty("Address1Id")
    @NotNull
    @Column(name = "external_id", length = 255, nullable = false)
    private String externalId;
    
    @JsonProperty("Address7Id")
    @Column(name = "alt_id", length = 255)
    private String altId;
    
    @JsonProperty("Address7Name")
    @NotNull
    @Column(name = "city", length = 255, nullable = false)
    private String city;
    
    @JsonProperty("Info1")
    @Column(name = "working_hours", length = 255)
    private String workingHours;
    
    @JsonProperty("Latitude")
    @Column(name = "latitude", length = 255)
    private String latitude;
    
    @JsonProperty("Longitude")
    @Column(name = "longitude")
    private String longitude;
    
    @JsonProperty("Note")
    @Column(name = "note", length = 3000)
    private String note;
    
    @JsonProperty("WarehouseName")
    @Column(name = "address", length = 255)
    private String address;
    
    @JsonProperty("WarehouseId")
    @Column(name = "warehouse_id", length = 255)
    private String warehouseId;
    
    @JsonProperty("isNew")
    @Column(name = "is_new", length = 255)
    private String isNew;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EuropostLocation)) {
            return false;
        }
        EuropostLocation other = (EuropostLocation) object;
        return !((this.getId() == null && other.getId() != null) 
            || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.agrolavka.EuropostLocation[ id=" + getId() + " ]";
    }

    @Override
    public int compareTo(EuropostLocation location) {
        if (location.getCity().compareTo(this.getCity()) > 0) {
            return -1;
        } else if (location.getCity().compareTo(this.getCity()) < 0) {
            return 1;
        } else {
            return location.getAddress().compareTo(this.getAddress()) > 0 ? -1 : 1;
        }
    }
}
