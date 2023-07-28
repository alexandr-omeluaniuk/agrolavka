package ss.agrolavka.rest.entity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.Feedback;

/**
 * Feedback REST controller.
 * @author alex
 */
@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/feedback")
public class FeedbackRestController extends BasicEntityRestController<Feedback> {

    @Override
    protected Class<Feedback> entityClass() {
        return Feedback.class;
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        coreDAO.delete(id, Feedback.class);
    }
}
