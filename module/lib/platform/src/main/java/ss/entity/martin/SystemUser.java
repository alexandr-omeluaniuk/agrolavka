package ss.entity.martin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ss.martin.core.anno.EntityAccess;
import ss.martin.core.anno.Updatable;
import ss.martin.core.constants.StandardRole;
import ss.martin.platform.security.SystemUserStatus;

/**
 * SystemUser.
 * @author Alexandr Omeluaniuk
 */
@Entity
@Table(name = "users")
@EntityAccess(roles = { StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR })
public class SystemUser extends TenantEntity implements SoftDeleted {
    /** Default UID. */
    private static final long serialVersionUID = 1L;
// ==================================== FIELDS ====================================================
    /** Email. */
    @Email
    @Updatable
    @NotEmpty
    @Size(max = 255)
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    /** Password. */
    @JsonIgnore
    @Size(max = 255)
    @Column(name = "password", length = 255)
    private String password;
    /** First name. */
    @Size(max = 255)
    @Updatable
    @Column(name = "firstname", length = 255)
    private String firstname;
    /** Last name. */
    @Size(max = 255)
    @Updatable
    @NotEmpty
    @Column(name = "lastname", nullable = false, length = 255)
    private String lastname;
    /** Status. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SystemUserStatus status;
    /** Standard role. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "standard_role", nullable = false)
    private StandardRole standardRole;
    /** Validation string for registration. */
    @Column(name = "validation_string")
    private String validationString;
    /** Active. */
    @Column(name = "active", nullable = false)
    private boolean active;
// ==================================== SET & GET =================================================
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @return the lastName
     */
    public String getLastname() {
        return lastname;
    }
    /**
     * @param lastname the lastName to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    /**
     * @return the standardRole
     */
    public StandardRole getStandardRole() {
        return standardRole;
    }
    /**
     * @param standardRole the standardRole to set
     */
    public void setStandardRole(StandardRole standardRole) {
        this.standardRole = standardRole;
    }
    /**
     * @return the validationString
     */
    public String getValidationString() {
        return validationString;
    }
    /**
     * @param validationString the validationString to set
     */
    public void setValidationString(String validationString) {
        this.validationString = validationString;
    }
    /**
     * @return the status
     */
    public SystemUserStatus getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(SystemUserStatus status) {
        this.status = status;
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
// ================================================================================================
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SystemUser)) {
            return false;
        }
        SystemUser other = (SystemUser) object;
        return !((this.getId() == null && other.getId() != null)
                || (this.getId() != null && !this.getId().equals(other.getId())));
    }
    @Override
    public String toString() {
        return "ss.entity.martin.SystemUser[ id=" + getId() + " ]";
    }
}
