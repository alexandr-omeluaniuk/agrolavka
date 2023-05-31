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
 * Address.
 * @author alex
 */
@Getter
@Setter
@Entity
@Table(name = "address")
public class Address extends DataModel {
    /** Region. */
    @NotNull
    @Size(max = 255)
    @Column(name = "region", length = 255)
    private String region;
    /** City. */
    @NotNull
    @Size(max = 255)
    @Column(name = "district", length = 255)
    private String district;
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
