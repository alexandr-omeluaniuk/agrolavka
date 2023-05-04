/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.json.JSONObject;

/**
 * Price type.
 * @author alex
 */
@Entity
@Table(name = "price_type")
public class PriceType extends ExternalEntity {
    // ================================================= FIELDS =======================================================
    /** Price type name. */
    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    // ================================================= SET & GET ====================================================
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
        if (!(object instanceof PriceType)) {
            return false;
        }
        PriceType other = (PriceType) object;
        if ((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
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
