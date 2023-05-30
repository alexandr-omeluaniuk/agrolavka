package ss.martin.platform.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.entity.martin.DataModel;
import ss.martin.platform.service.EntityService;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;
import ss.martin.security.constants.PlatformUrl;
import ss.martin.security.model.RestResponse;

/**
 * Entity REST controller.
 * @author ss
 */
@RestController
@RequestMapping(PlatformUrl.ENTITY_URL)
public class EntityRestController {
    /** Entity service. */
    @Autowired
    private EntityService entityService;
    
    /**
     * Search entities.
     * @param entityName entity name.
     * @param page page number.
     * @param pageSize page size.
     * @param order sort order.
     * @param orderBy order by field.
     * @return search response.
     * @throws Exception error.
     */
    @RequestMapping(value = "/{entity}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EntitySearchResponse list(@PathVariable("entity") String entityName,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "order_by", required = false) String orderBy
    ) throws Exception {
        Class entityClass = getEntityClass(entityName);
        return entityService.list(entityClass, new EntitySearchRequest(
                page == null ? 1 : page,
                pageSize == null ? Integer.MAX_VALUE : pageSize,
                order,
                orderBy
        ));
    }
    
    /**
     * Get entity by ID.
     * @param entityName entity name.
     * @param id entity ID.
     * @return entity.
     * @throws Exception error.
     */
    @RequestMapping(value = "/{entity}/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataModel get(@PathVariable("entity") String entityName, @PathVariable("id") Long id) throws Exception {
        Class entityClass = getEntityClass(entityName);
        return entityService.get(id, entityClass);
    }
    
    /**
     * Create entity.
     * @param entityName entity name.
     * @param rawData raw data.
     * @return entity with ID.
     * @throws Exception error.
     */
    @RequestMapping(value = "/{entity}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataModel create(@PathVariable("entity") String entityName, @RequestBody Object rawData)
            throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class entityClass = getEntityClass(entityName);
        DataModel entity = (DataModel) mapper.convertValue(rawData, entityClass);
        return entityService.create(entity);
    }
    
    /**
     * Update entity.
     * @param entityName entity name.
     * @param rawData raw data.
     * @return empty response.
     * @throws Exception error.
     */
    @RequestMapping(value = "/{entity}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataModel update(@PathVariable("entity") String entityName, @RequestBody Object rawData)
            throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class entityClass = getEntityClass(entityName);
        DataModel entity = (DataModel) mapper.convertValue(rawData, entityClass);
        return entityService.update(entity);
    }
    
    /**
     * Mass deletion.
     * @param entityName entity name.
     * @param id entity ID.
     * @return response.
     * @throws Exception error.
     */
    @RequestMapping(value = "/{entity}/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse delete(@PathVariable("entity") String entityName, @PathVariable("id") Long id)
            throws Exception {
        entityService.delete(new HashSet(Arrays.asList(new Long[] {id})), getEntityClass(entityName));
        return new RestResponse();
    }
    
    protected Class getEntityClass(String name) throws Exception {
        return Class.forName(name);
    }
}