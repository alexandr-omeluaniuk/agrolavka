package ss.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ss.entity.martin.SoftDeleted;
import ss.martin.core.anno.EntityAccess;
import ss.martin.core.constants.StandardRole;
import ss.martin.security.constants.SystemUserStatus;

/**
 * SystemUser.
 * @author Alexandr Omeluaniuk
 */
@Entity
@Table(name = "users")
@EntityAccess(roles = { StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR })
public class SystemUser extends TenantEntity implements SoftDeleted {
    /** Email. */
    @Email
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
    @Column(name = "firstname", length = 255)
    private String firstname;
    /** Last name. */
    @Size(max = 255)
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public StandardRole getStandardRole() {
        return standardRole;
    }
    
    public void setStandardRole(StandardRole standardRole) {
        this.standardRole = standardRole;
    }
    
    public String getValidationString() {
        return validationString;
    }
    
    public void setValidationString(String validationString) {
        this.validationString = validationString;
    }
    
    public SystemUserStatus getStatus() {
        return status;
    }
    
    public void setStatus(SystemUserStatus status) {
        this.status = status;
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
    
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
