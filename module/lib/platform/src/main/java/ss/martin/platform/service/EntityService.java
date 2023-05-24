package ss.martin.platform.service;

import java.util.Set;
import ss.entity.martin.DataModel;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;

/**
 * Entity service.
 * @author ss
 */
public interface EntityService {
    
    /**
     * Search entities.
     * @param clazz entity class.
     * @param searchRequest search request.
     * @return search response.
     */
    EntitySearchResponse list(Class<? extends DataModel> clazz, EntitySearchRequest searchRequest);
    
    /**
     * Create entity.
     * @param entity entity.
     * @return entity.
     */
    DataModel create(DataModel entity);
    
    /**
     * Update entity.
     * @param <T> entity type.
     * @param entity entity.
     * @return updated entity.
     */
    <T extends DataModel> T update(T entity);
    
    /**
     * Delete entities.
     * @param <T> entity type.
     * @param ids set of IDs.
     * @param cl entity class.
     */
    <T extends DataModel> void delete(Set<Long> ids, Class<T> cl);
    
    /**
     * Find entity by ID.
     * @param <T> entity type.
     * @param id entity ID.
     * @param cl entity class.
     * @return entity.
     * @throws Exception error.
     */
    <T extends DataModel> T get(Long id, Class<T> cl);
}
