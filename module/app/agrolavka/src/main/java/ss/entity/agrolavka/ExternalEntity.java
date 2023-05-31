package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ss.entity.security.EntityAudit;

/**
 * External Entity.
 * @author alex
 */
@Getter
@Setter
@MappedSuperclass
public abstract class ExternalEntity extends EntityAudit {
    /** External ID. */
    @NotNull
    @Size(max = 255)
    @Column(name = "external_id", length = 255, nullable = false)
    private String externalId;
}
