package ss.martin.platform.util;

import java.util.Set;
import ss.entity.martin.DataModel;

/**
 * Platform entity listener.
 * @author alex
 * @param <E> entity type.
 */
public interface PlatformEntityListener<E extends DataModel> {
    
    /**
     * Get listener target entity class.
     * @return entity class.
     */
    Class<E> entity();
    
    /**
     * Invoked before persist action.
     * @param entity entity.
     */
    default void prePersist(E entity) {
    }
    
    /**
     * Invoked after persist action.
     * @param entity entity.
     */
    default void postPersist(E entity) {
    }
    
    /**
     * Invoked before update action.
     * @param entity entity.
     */
    default void preUpdate(E entity) {
    }
    
    /**
     * Invoked after update action.
     * @param entity entity.
     */
    default void postUpdate(E entity) {
    }
    
    /**
     * Invoked before delete action.
     * @param ids entity IDs.
     */
    default void preDelete(Set<Long> ids) {
    }
    
    /**
     * Invoked after delete action.
     * @param ids entity IDs.
     */
    default void postDelete(Set<Long> ids) {
    }
}
