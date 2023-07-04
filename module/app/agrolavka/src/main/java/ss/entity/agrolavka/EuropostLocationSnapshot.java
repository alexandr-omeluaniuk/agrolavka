package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ss.entity.martin.DataModel;

/**
 * Europost location snapshot.
 * @author alex
 */
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
    @Column(name = "working_hours", length = 1024)
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }
    
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
