package ss.agrolavka.rest.entity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.Slide;

/**
 * Slides REST
 * @author alex
 */
@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/slide")
public class SlideRestController extends BasicEntityWithImagesRestController<Slide> {

    @Override
    protected Class<Slide> entityClass() {
        return Slide.class;
    }
    
    @Override
    protected String cacheKey() {
        return CacheKey.SLIDES;
    }
}
