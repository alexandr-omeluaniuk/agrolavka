package ss.agrolavka.rest.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.util.AppCache;
import ss.entity.agrolavka.Shop;
import ss.entity.images.storage.EntityImage;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;

/**
 * REST controller for Shop entity.
 * @author alex
 */
@RestController
@RequestMapping(SiteConstants.URL_PROTECTED + "/shop")
public class ShopRestController {
    
    @Autowired
    private CoreDao coreDAO;
    
    @GetMapping
    public EntitySearchResponse list(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "order_by", required = false) String orderBy
    ) {
        return coreDAO.searchEntities(
            new EntitySearchRequest(
                page == null ? 1 : page,
                pageSize == null ? Integer.MAX_VALUE : pageSize,
                order,
                orderBy
            ), 
            Shop.class
        );
    }
    
    @GetMapping("/{id}")
    public Shop get(@PathVariable("id") final Long id) {
        return coreDAO.findById(id, Shop.class);
    }
    
    @PostMapping
    public Shop create(@RequestBody final Shop shop) {
        final var newShop = coreDAO.create(shop);
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
        return newShop;
    }
    
    @PutMapping
    public Shop update(@RequestBody final Shop shop) {
        final var entityFromDB = coreDAO.findById(shop.getId(), Shop.class);
        shop.setImages(
            EntityImage.getActualImages(entityFromDB.getImages(), shop.getImages())
        );
        final var updatedShop = coreDAO.update(shop);
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
        return updatedShop;
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        coreDAO.delete(id, Shop.class);
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }
}
