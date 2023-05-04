package ss.martin.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import ss.entity.martin.DataModel;
import ss.entity.martin.SoftDeleted;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;

/**
 * Core DAO API.
 * @author Alexandr Omeluaniuk
 */
public interface CoreDao {
    /**
     * Create entity (subscription will be assigned automatically).
     * @param <T> entity class.
     * @param entity entity.
     * @return created entity.
     */
    <T extends DataModel> T create(T entity);
    
    /**
     * Update entity (subscription will be assigned automatically).
     * @param <T> entity class.
     * @param entity entity.
     * @return updated entity.
     */
    <T extends DataModel> T update(T entity);
    
    /**
     * Find entity by ID.
     * @param <T> entity type.
     * @param id entity ID.
     * @param cl entity class.
     * @return entity.
     */
    <T extends DataModel> T findById(Serializable id, Class<T> cl);
    
    /**
     * Delete entity.
     * @param <T> entity type.
     * @param id entity ID.
     * @param cl entity class.
     */
    <T extends DataModel> void delete(Serializable id, Class<T> cl);
    
    /**
     * Mass deletion.
     * @param <T> entity type.
     * @param ids set of IDs.
     * @param cl entity class.
     */
    <T extends DataModel> void massDelete(Set<Long> ids, Class<T> cl);
    
    /**
     * Mass deletion.
     * @param <T> entity type.
     * @param entities entities.
     */
    <T extends DataModel> void massDelete(List<T> entities);
    
    /**
     * Mass create.
     * @param <T> entity type.
     * @param list list of entities.
     */
    <T extends DataModel> void massCreate(List<T> list);
    
    /**
     * Mass update.
     * @param <T> entity type.
     * @param list list of entities.
     */
    <T extends DataModel> void massUpdate(List<T> list);
    
    /**
     * Search entities.
     * @param <T> entity type.
     * @param cl entity class.
     * @param searchRequest search request.
     * @return search response.
     */
    <T extends DataModel> EntitySearchResponse searchEntities(Class<T> cl, EntitySearchRequest searchRequest);
    
    /**
     * Deactivate entities.
     * @param <T> entity type.
     * @param ids entity IDs.
     * @param cl entity class.
     */
    <T extends DataModel & SoftDeleted> void deactivateEntities(Set<Long> ids, Class<T> cl);
    
    /**
     * Activate entities.
     * @param <T> entity type.
     * @param ids entity IDs.
     * @param cl entity class.
     */
    <T extends DataModel & SoftDeleted> void activateEntities(Set<Long> ids, Class<T> cl);
    
    /**
     * Count.
     * @param <T> entity type.
     * @param cl entity class.
     * @return count of entities.
     * @throws Exception error.
     */
    <T extends DataModel> Long count(Class<T> cl) throws Exception;
    
    /**
     * Get all entities.
     * @param <T> entity type.
     * @param cl entity class.
     * @return list of entities.
     * @throws Exception error.
     */
    <T extends DataModel> List<T> getAll(Class<T> cl) throws Exception;
}
