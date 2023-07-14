package ss.agrolavka.rest.entity;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ss.entity.martin.DataModel;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;

/**
 * Basic REST controller for entity.
 * @author alex
 * @param <T>
 */
public abstract class BasicEntityRestController<T extends DataModel> {
    
    @Autowired
    protected CoreDao coreDAO;
    
    protected abstract Class<T> entityClass();
    
    @GetMapping
    public EntitySearchResponse<T> list(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "order_by", required = false) String orderBy
    ) {
        return coreDAO.searchEntities(
            new EntitySearchRequest(
                Optional.ofNullable(page).orElse(1),
                Optional.ofNullable(pageSize).orElse(Integer.MAX_VALUE),
                order,
                orderBy
            ), 
            entityClass()
        );
    }
    
    @GetMapping("/{id}")
    public T get(@PathVariable("id") final Long id) {
        return coreDAO.findById(id, entityClass());
    }
}
