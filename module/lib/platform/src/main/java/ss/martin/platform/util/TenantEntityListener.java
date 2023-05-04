/*
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.util;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ss.entity.martin.SystemUser;
import ss.entity.martin.TenantEntity;
import ss.martin.platform.security.SecurityContext;
import ss.martin.core.constants.StandardRole;
import ss.martin.platform.service.impl.ReflectionUtils;

/**
 * Tenant entity listener.
 * @author ss
 */
public class TenantEntityListener {
    @PrePersist
    @PreUpdate
    protected void prePersistAndUpdate(TenantEntity entity) throws Exception {
        // exceptions for system user entity
        if (entity instanceof SystemUser) {
            StandardRole role = ((SystemUser) entity).getStandardRole();
            // case 1: allows ignore subscription during super admin creation process.
            if (StandardRole.ROLE_SUPER_ADMIN.equals(role)) {
                return;
            }
            // case 2: user try to finish registration
            Object auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof AnonymousAuthenticationToken) {
                return;
            }
            // case 3: super admin creates new subscription and subscription administrator
            if (StandardRole.ROLE_SUPER_ADMIN.equals(SecurityContext.currentUser().getStandardRole())) {
                return;
            }
        }
        // save current subscription for every tenant entity.
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        if (entity != null && ReflectionUtils.hasSuperClass(entity.getClass(), TenantEntity.class)) {
            TenantEntity tenantEntity = (TenantEntity) entity;
            tenantEntity.setSubscription(SecurityContext.subscription());
        }
    }
}
