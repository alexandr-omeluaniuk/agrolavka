package ss.agrolavka.rest.entity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.SiteConstants;
import ss.entity.agrolavka.Slide;

/**
 *
 * @author alex
 */
@RestController
@RequestMapping(SiteConstants.URL_PROTECTED + "/slide")
public class SlideRestController extends BasicEntityWithImagesRestController<Slide> {

    @Override
    protected Class<Slide> entityClass() {
        return Slide.class;
    }
    
}
