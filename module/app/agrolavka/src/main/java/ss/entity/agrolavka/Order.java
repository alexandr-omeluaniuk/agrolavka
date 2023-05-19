/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import ss.agrolavka.constants.OrderStatus;
import ss.entity.martin.DataModel;

/**
 * Customer order.
 * @author alex
 */
@Entity
@Table(name = "customer_order")
public class Order extends DataModel {
    // =============================================== FIELDS =========================================================
    /** Order positions. */
    @Size(min = 1)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderPosition> positions;
    /** Address. */
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    /** Europost location. */
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "europost_location_snapshot_id")
    private EuropostLocationSnapshot europostLocationSnapshot;
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
    /** Comment. */
    @Length(max = 10000)
    @Column(name = "comment", length = 10000)
    private String comment;
    /** Admin comment. */
    @Length(max = 3000)
    @Column(name = "admin_comment", length = 3000)
    private String adminComment;
    /** Order status. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;
    /** One click order. */
    @Column(name = "one_click")
    private Boolean oneClick;
    // =============================================== SET & GET ======================================================
    /**
     * @return the positions
     */
    public List<OrderPosition> getPositions() {
        return positions;
    }
    /**
     * @param positions the positions to set
     */
    public void setPositions(List<OrderPosition> positions) {
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
    /**
     * @return the status
     */
    public OrderStatus getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }
    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    /**
     * @return the adminComment
     */
    public String getAdminComment() {
        return adminComment;
    }
    /**
     * @param adminComment the adminComment to set
     */
    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }
    /**
     * @return the europostLocationSnapshot
     */
    public EuropostLocationSnapshot getEuropostLocationSnapshot() {
        return europostLocationSnapshot;
    }
    /**
     * @param europostLocationSnapshot the europostLocationSnapshot to set
     */
    public void setEuropostLocationSnapshot(EuropostLocationSnapshot europostLocationSnapshot) {
        this.europostLocationSnapshot = europostLocationSnapshot;
    }
    /**
     * @return the oneClick
     */
    public Boolean getOneClick() {
        return oneClick;
    }
    /**
     * @param oneClick the oneClick to set
     */
    public void setOneClick(Boolean oneClick) {
        this.oneClick = oneClick;
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