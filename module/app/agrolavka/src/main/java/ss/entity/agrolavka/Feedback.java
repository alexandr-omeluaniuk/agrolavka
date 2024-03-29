package ss.entity.agrolavka;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import ss.entity.martin.DataModel;

/**
 * User feedback.
 * @author alex
 */
@Entity
@Table(name = "feedback")
public class Feedback extends DataModel {
    /** Message. */
    @NotNull
    @Length(min = 1, max = 10000)
    @Column(name = "message", length = 10000, nullable = false)
    private String message;
    /** Contact info. */
    @Length(max = 255)
    @Column(name = "contact", length = 255)
    private String contact;
    /** Created. */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;
    
    public String getMessage() {
        return message;
    }
    
    public Date getCreated() {
        return created;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Feedback)) {
            return false;
        }
        Feedback other = (Feedback) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.agrolavka.Feedback[ id=" + getId() + " ]";
    }
}
