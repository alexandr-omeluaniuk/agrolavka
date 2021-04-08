/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ss.entity.martin.DataModel;

/**
 * Customer order.
 * @author alex
 */
@Entity
@Table(name = "customer_order")
public class Order extends DataModel{
    // =============================================== FIELDS =========================================================
    /** Order positions. */
    @Size(min = 1)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderPosition> positions;
    /** Address. */
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    /** Created. */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;
    /** Phone number. */
    @NotNull
    @Size(max = 255)
    @Column(name = "phone", nullable = false, length = 255)
    private String phone;
    // =============================================== SET & GET ======================================================
    /**
     * @return the positions
     */
    public Set<OrderPosition> getPositions() {
        return positions;
    }
    /**
     * @param positions the positions to set
     */
    public void setPositions(Set<OrderPosition> positions) {
        this.positions = positions;
    }
    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }
    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }
    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }
    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }
    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
        if (!(object instanceof Order)) {
            return false;
        }
        Order other = (Order) object;
        if ((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "ss.entity.agrolavka.Order[ id=" + getId() + " ]";
    }
}
