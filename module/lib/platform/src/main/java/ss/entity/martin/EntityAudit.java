package ss.entity.martin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity audit superclass.
 * @author Alexandr Omeluaniuk
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntityAudit extends TenantEntity {
// ================================== FIELDS ======================================================
    /** Created by. */
    @CreatedBy
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private SystemUser createdBy;
    /** Created date. */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;
    /** Last modified by. */
    @LastModifiedBy
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private SystemUser lastModifiedBy;
    /** Last modified date. */
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
// ================================= SET & GET ====================================================
    /**
     * @return the createdBy
     */
    public SystemUser getCreatedBy() {
        return createdBy;
    }
    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(SystemUser createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }
    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    /**
     * @return the lastModifiedBy
     */
    public SystemUser getLastModifiedBy() {
        return lastModifiedBy;
    }
    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    public void setLastModifiedBy(SystemUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
