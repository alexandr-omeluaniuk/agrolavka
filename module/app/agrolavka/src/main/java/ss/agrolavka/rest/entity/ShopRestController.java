package ss.agrolavka.rest.entity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.Shop;

/**
 * REST controller for Shop entity.
 * @author alex
 */
@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/shop")
public class ShopRestController extends BasicEntityWithImagesRestController<Shop> {

    @Override
    protected Class<Shop> entityClass() {
        return Shop.class;
    }
    
    @Override
    protected String cacheKey() {
        return CacheKey.SHOPS;
    }
}
