package ss.agrolavka.rest.entity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.SiteConstants;
import ss.entity.agrolavka.Shop;

/**
 * REST controller for Shop entity.
 * @author alex
 */
@RestController
@RequestMapping(SiteConstants.URL_PROTECTED + "/shop")
public class ShopRestController extends BasicEntityWithImagesRestController<Shop> {

    @Override
    protected Class<Shop> entityClass() {
        return Shop.class;
    }
}
