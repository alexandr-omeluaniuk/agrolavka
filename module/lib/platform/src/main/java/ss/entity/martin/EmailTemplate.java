package ss.entity.martin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ss.martin.core.anno.Updatable;

/**
 * Email template.
 * @author ss
 */
@Entity
@Table(name = "email_template")
public class EmailTemplate extends EntityAudit implements SoftDeleted {
    /** Default UID. */
    private static final long serialVersionUID = 1L;
// =============================================== FIELDS =============================================================
    /** Email subject. */
    @Updatable
    @NotNull
    @Size(max = 255)
    @Column(name = "subject", nullable = false, length = 255)
    private String subject;
    /** Email content. */
    @Updatable
    @NotNull
    @Size(max = 2147483647)
    @Lob
    @Column(name = "content", nullable = false, length = 2147483647)
    private String content;
    /** Active. */
    @Column(name = "active", nullable = false)
    private boolean active;
// =============================================== SET & GET ==========================================================
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @return the active
     */
    @Override
    public boolean isActive() {
        return active;
    }
    /**
     * @param active the active to set
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
// ====================================================================================================================
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EmailTemplate)) {
            return false;
        }
        EmailTemplate other = (EmailTemplate) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    @Override
    public String toString() {
        return "ss.entity.martin.EmailTemplate[ id=" + getId() + " ]";
    }
}
