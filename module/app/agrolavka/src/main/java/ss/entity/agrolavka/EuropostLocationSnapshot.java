/*
 * Copyright (C) 2022 alex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    // ============================================== FIELDS ==========================================================
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
    // ============================================== SET & GET =======================================================
    /**
     * @return the externalId
     */
    public String getExternalId() {
        return externalId;
    }
    /**
     * @param externalId the externalId to set
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    /**
     * @return the altId
     */
    public String getAltId() {
        return altId;
    }
    /**
     * @param altId the altId to set
     */
    public void setAltId(String altId) {
        this.altId = altId;
    }
    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }
    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * @return the workingHours
     */
    public String getWorkingHours() {
        return workingHours;
    }
    /**
     * @param workingHours the workingHours to set
     */
    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }
    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }
    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }
    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }
    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return the warehouseId
     */
    public String getWarehouseId() {
        return warehouseId;
    }
    /**
     * @param warehouseId the warehouseId to set
     */
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
    /**
     * @return the isNew
     */
    public String getIsNew() {
        return isNew;
    }
    /**
     * @param isNew the isNew to set
     */
    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }
    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }
    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    /**
     * @return the middlename
     */
    public String getMiddlename() {
        return middlename;
    }
    /**
     * @param middlename the middlename to set
     */
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }
    // ================================================================================================================
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
