/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ss.entity.security.EntityAudit;

/**
 * External Entity.
 * @author alex
 */
@MappedSuperclass
public abstract class ExternalEntity extends EntityAudit {
    /** External ID. */
    @NotNull
    @Size(max = 255)
    @Column(name = "external_id", length = 255, nullable = false)
    private String externalId;
    /**
     * @return the externalId
     */
    public String getExternalId() {
        return externalId;
    }
    /**
     * @param externalId the externalId to set
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
