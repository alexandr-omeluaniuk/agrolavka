package ss.martin.platform.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.entity.martin.DataModel;
import ss.entity.martin.SoftDeleted;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.base.exception.PlatformException;
import ss.martin.base.lang.ThrowingRunnable;
import ss.martin.core.anno.Updatable;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;
import ss.martin.platform.exception.PlatformSecurityException;
import ss.martin.platform.service.EntityService;
import ss.martin.platform.util.PlatformEntityListener;
import ss.martin.platform.service.SecurityService;
import ss.martin.platform.constants.EntityPermission;
import ss.martin.security.api.RegistrationUserService;

/**
 * Entity service implementation.
 * @author ss
 */
@Service
class EntityServiceImpl implements EntityService {
    /** Core DAO. */
    @Autowired
    private CoreDao coreDao;
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
    public EntitySearchResponse list(
        final Class<? extends DataModel> clazz, 
        final EntitySearchRequest searchRequest
    ) {
        if (!securityService.getEntityPermissions(clazz).contains(EntityPermission.READ)) {
            throw new PlatformSecurityException(EntityPermission.READ, clazz);
        }
        return coreDao.searchEntities(searchRequest, clazz);
    }
    
    @Override
    public <T extends DataModel> T create(final T entity) {
        if (!securityService.getEntityPermissions(entity.getClass()).contains(EntityPermission.CREATE)) {
            throw new PlatformSecurityException(EntityPermission.CREATE, entity.getClass());
        }
        if (SoftDeleted.class.isAssignableFrom(entity.getClass())) {
            ((SoftDeleted) entity).setActive(true);
        }
        if (entity instanceof Subscription subscription) {
            return (T) systemUserService.createSubscriptionAndAdmin(subscription);
        } else if (entity instanceof SystemUser user) {
            return (T) systemUserService.createSubscriptionUser(user);
        } else {
            List<PlatformEntityListener> listeners = getEntityListener(entity.getClass());
            for (PlatformEntityListener l : listeners) {
                l.prePersist(entity);
            }
            final var entityCreated = coreDao.create(entity);
            for (PlatformEntityListener l : listeners) {
                l.postPersist(entityCreated);
            }
            return entityCreated;
        }
    }
    
    @Override
    public <T extends DataModel> T update(T entity) {
        if (!securityService.getEntityPermissions(entity.getClass()).contains(EntityPermission.UPDATE)) {
            throw new PlatformSecurityException(EntityPermission.UPDATE, entity.getClass());
        }
        Class<T> entityClass = (Class<T>) entity.getClass();
        T fromDb = coreDao.findById(entity.getId(), entityClass);
        setUpdatableFields(entityClass, fromDb, entity);
        List<PlatformEntityListener> listeners = getEntityListener(entity.getClass());
        for (PlatformEntityListener l : listeners) {
            l.preUpdate(entity);
        }
        entity = coreDao.update(fromDb);
        for (PlatformEntityListener l : listeners) {
            l.postUpdate(entity);
        }
        return coreDao.findById(entity.getId(), entityClass);
    }
    
    @Override
    public <T extends DataModel> void delete(Set<Long> ids, Class<T> cl) {
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
        coreDao.massDelete(ids, cl);
        for (PlatformEntityListener l : listeners) {
            l.postDelete(ids);
        }
    }
    
    @Override
    public <T extends DataModel> T get(Long id, Class<T> cl) {
        if (!securityService.getEntityPermissions(cl).contains(EntityPermission.READ)) {
            throw new PlatformSecurityException(EntityPermission.READ, cl);
        }
        return coreDao.findById(id, cl);
    }
    
    /**
     * Set values for updatable fields.
     * @param entityClass entity class.
     * @param fromDb entity from database.
     * @param fromUser entity from user.
     * @throws Exception error.
     */
    private void setUpdatableFields(Class entityClass, Object fromDb, Object fromUser) {
        ((ThrowingRunnable) () -> {
            for (Field field : entityClass.getDeclaredFields()) {
                Updatable formField = field.getAnnotation(Updatable.class);
                if (formField != null) {    // field is updatable
                    field.setAccessible(true);
                    field.set(fromDb, field.get(fromUser));
                    field.setAccessible(false);
                }
            }
            if (entityClass.getSuperclass() != null) {
                setUpdatableFields(entityClass.getSuperclass(), fromDb, fromUser);
            }
        }).run();
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
