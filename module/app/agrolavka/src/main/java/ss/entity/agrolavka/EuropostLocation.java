package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import ss.entity.martin.DataModel;

/**
 * Europost location.
 * @author alex
 */
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
    
    public String getExternalId() {
        return externalId;
    }
    
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    
    public String getAltId() {
        return altId;
    }
    
    public void setAltId(String altId) {
        this.altId = altId;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getWorkingHours() {
        return workingHours;
    }
    
    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }
    
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
    
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
