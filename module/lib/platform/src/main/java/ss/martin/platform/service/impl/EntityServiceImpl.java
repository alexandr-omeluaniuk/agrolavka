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
package ss.martin.platform.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ss.entity.martin.DataModel;
import ss.entity.martin.SoftDeleted;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.base.exception.PlatformException;
import ss.martin.core.anno.Updatable;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;
import ss.martin.platform.exception.PlatformSecurityException;
import ss.martin.platform.service.EntityService;
import ss.martin.platform.util.PlatformEntityListener;
import ss.martin.security.api.SecurityService;
import ss.martin.security.constants.EntityPermission;
import ss.martin.security.api.RegistrationUserService;

/**
 * Entity service implementation.
 * @author ss
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class EntityServiceImpl implements EntityService {
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    /** System user service. */
    @Autowired
    private RegistrationUserService systemUserService;
    /** Security service. */
    @Autowired
    private SecurityService securityService;
    /** Platform entity listeners. */
    @Autowired
    private List<PlatformEntityListener> entityListeners;
    @Override
    public EntitySearchResponse list(Class<? extends DataModel> clazz,
            EntitySearchRequest searchRequest) throws Exception {
        if (!securityService.getEntityPermissions(clazz).contains(EntityPermission.READ)) {
            throw new PlatformSecurityException(EntityPermission.READ, clazz);
        }
        return coreDAO.searchEntities(searchRequest, clazz);
    }
    @Override
    public <T extends DataModel> T create(T entity) throws Exception {
        if (!securityService.getEntityPermissions(entity.getClass()).contains(EntityPermission.CREATE)) {
            throw new PlatformSecurityException(EntityPermission.CREATE, entity.getClass());
        }
        if (SoftDeleted.class.isAssignableFrom(entity.getClass())) {
            ((SoftDeleted) entity).setActive(true);
        }
        if (entity instanceof Subscription) {
            return (T) systemUserService.createSubscriptionAndAdmin((Subscription) entity);
        } else if (entity instanceof SystemUser) {
            return (T) systemUserService.createSubscriptionUser((SystemUser) entity);
        } else {
            List<PlatformEntityListener> listeners = getEntityListener(entity.getClass());
            for (PlatformEntityListener l : listeners) {
                l.prePersist(entity);
            }
            entity = coreDAO.create(entity);
            for (PlatformEntityListener l : listeners) {
                l.postPersist(entity);
            }
            return entity;
        }
    }
    @Override
    public <T extends DataModel> T update(T entity) throws Exception {
        if (!securityService.getEntityPermissions(entity.getClass()).contains(EntityPermission.UPDATE)) {
            throw new PlatformSecurityException(EntityPermission.UPDATE, entity.getClass());
        }
        Class<T> entityClass = (Class<T>) entity.getClass();
        T fromDB = coreDAO.findById(entity.getId(), entityClass);
        setUpdatableFields(entityClass, fromDB, entity);
        List<PlatformEntityListener> listeners = getEntityListener(entity.getClass());
        for (PlatformEntityListener l : listeners) {
            l.preUpdate(entity);
        }
        entity = coreDAO.update(fromDB);
        for (PlatformEntityListener l : listeners) {
            l.postUpdate(entity);
        }
        return coreDAO.findById(entity.getId(), entityClass);
    }
    @Override
    public <T extends DataModel> void delete(Set<Long> ids, Class<T> cl) throws Exception {
        if (!securityService.getEntityPermissions(cl).contains(EntityPermission.DELETE)) {
            throw new PlatformSecurityException(EntityPermission.DELETE, cl);
        }
        if (SoftDeleted.class.isAssignableFrom(cl)) {
            throw new PlatformException("Attempt to delete undeletable entity: " + cl.getName());
        }
        List<PlatformEntityListener> listeners = getEntityListener(cl);
        for (PlatformEntityListener l : listeners) {
            l.preDelete(ids);
        }
        coreDAO.massDelete(ids, cl);
        for (PlatformEntityListener l : listeners) {
            l.postDelete(ids);
        }
    }
    @Override
    public <T extends DataModel> T get(Long id, Class<T> cl) throws Exception {
        if (!securityService.getEntityPermissions(cl).contains(EntityPermission.READ)) {
            throw new PlatformSecurityException(EntityPermission.READ, cl);
        }
        return coreDAO.findById(id, cl);
    }
    // ==================================== PRIVATE ===================================================================
    /**
     * Set values for updatable fields.
     * @param entityClass entity class.
     * @param fromDB entity from database.
     * @param fromUser entity from user.
     * @throws Exception error.
     */
    private void setUpdatableFields(Class entityClass, Object fromDB, Object fromUser) throws Exception {
        for (Field field : entityClass.getDeclaredFields()) {
            Updatable formField = field.getAnnotation(Updatable.class);
            if (formField != null) {    // field is updatable
                field.setAccessible(true);
                field.set(fromDB, field.get(fromUser));
                field.setAccessible(false);
            }
        }
        if (entityClass.getSuperclass() != null) {
            setUpdatableFields(entityClass.getSuperclass(), fromDB, fromUser);
        }
    }
    /**
     * Get platform entity listener.
     * @param cl entity class.
     * @return list of listeners.
     */
    private List<PlatformEntityListener> getEntityListener(Class<? extends DataModel> cl) {
        return entityListeners.stream().filter(l -> {
            try {
                return cl.equals(l.entity()) || ReflectionUtils.hasSuperClass(cl, l.entity());
            } catch (Exception ex) {
                return false;
            }
        }).collect(Collectors.toList());
    }
}
