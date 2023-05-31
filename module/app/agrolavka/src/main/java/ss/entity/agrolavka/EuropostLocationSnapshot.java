package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ss.entity.martin.DataModel;

/**
 * Europost location snapshot.
 * @author alex
 */
@Getter
@Setter
@Entity
@Table(name = "europost_location_snapshot")
public class EuropostLocationSnapshot extends DataModel {
    @NotNull
    @Column(name = "external_id", length = 255, nullable = false)
    private String externalId;
    
    @Column(name = "alt_id", length = 255)
    private String altId;
    
    @NotNull
    @Column(name = "city", length = 255, nullable = false)
    private String city;
    
    @Column(name = "working_hours", length = 255)
    private String workingHours;
    
    @Column(name = "latitude", length = 255)
    private String latitude;
    
    @Column(name = "longitude")
    private String longitude;
    
    @Column(name = "note", length = 3000)
    private String note;
    
    @Column(name = "address", length = 255)
    private String address;
    
    @Column(name = "warehouse_id", length = 255)
    private String warehouseId;
    
    @Column(name = "is_new", length = 255)
    private String isNew;
    
    /** First name. */
    @Size(max = 255)
    @Column(name = "firstname", length = 255)
    private String firstname;
    /** Last name. */
    @Size(max = 255)
    @Column(name = "lastname", length = 255)
    private String lastname;
    /** Middle name. */
    @Size(max = 255)
    @Column(name = "middlename", length = 255)
    private String middlename;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EuropostLocationSnapshot)) {
            return false;
        }
        EuropostLocationSnapshot other = (EuropostLocationSnapshot) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.agrolavka.EuropostLocationSnapshot[ id=" + getId() + " ]";
    }
}
