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
    /** References to Telegram messages. */
    @Column(name = "telegram_messages")
    private Long[] telegramMessages;
    
    public List<OrderPosition> getPositions() {
        return positions;
    }
    
    public void setPositions(List<OrderPosition> positions) {
        this.positions = positions;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Date getCreated() {
        return created;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getAdminComment() {
        return adminComment;
    }
    
    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }
    
    public EuropostLocationSnapshot getEuropostLocationSnapshot() {
        return europostLocationSnapshot;
    }
    
    public void setEuropostLocationSnapshot(EuropostLocationSnapshot europostLocationSnapshot) {
        this.europostLocationSnapshot = europostLocationSnapshot;
    }

    public Long[] getTelegramMessages() {
        return telegramMessages;
    }

    public void setTelegramMessages(Long[] telegramMessages) {
        this.telegramMessages = telegramMessages;
    }
    
    public Boolean getOneClick() {
        return oneClick;
    }
    
    public void setOneClick(Boolean oneClick) {
        this.oneClick = oneClick;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Order)) {
            return false;
        }
        Order other = (Order) object;
        return !((this.getId() == null && other.getId() != null)
            || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.agrolavka.Order[ id=" + getId() + " ]";
    }
}
