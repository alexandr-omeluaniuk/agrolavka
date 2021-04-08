/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ss.entity.martin.DataModel;

/**
 * Address.
 * @author alex
 */
@Entity
@Table(name = "address")
public class Address extends DataModel {
    // ================================================ FIELDS ========================================================
    /** City. */
    @NotNull
    @Size(max = 255)
    @Column(name = "city", length = 255)
    private String city;
    /** Street. */
    @NotNull
    @Size(max = 255)
    @Column(name = "street", length = 255)
    private String street;
    /** House. */
    @NotNull
    @Size(max = 255)
    @Column(name = "house", length = 255)
    private String house;
    /** Flat. */
    @Size(max = 255)
    @Column(name = "flat", length = 255)
    private String flat;
    /** Postcode. */
    @Size(max = 6)
    @Column(name = "postcode", length = 6)
    private String postcode;
    /** Recipient. */
    @Size(max = 255)
    @Column(name = "recipient", length = 255)
    private String recipient;
    // ================================================ SET & GET =====================================================
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
     * @return the street
     */
    public String getStreet() {
        return street;
    }
    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }
    /**
     * @return the house
     */
    public String getHouse() {
        return house;
    }
    /**
     * @param house the house to set
     */
    public void setHouse(String house) {
        this.house = house;
    }
    /**
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }
    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    /**
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }
    /**
     * @param recipient the recipient to set
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    /**
     * @return the flat
     */
    public String getFlat() {
        return flat;
    }
    /**
     * @param flat the flat to set
     */
    public void setFlat(String flat) {
        this.flat = flat;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "ss.entity.agrolavka.Address[ id=" + getId() + " ]";
    }
}
