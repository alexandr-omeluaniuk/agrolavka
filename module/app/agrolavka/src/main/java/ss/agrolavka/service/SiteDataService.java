package ss.agrolavka.service;

import java.util.List;
import ss.entity.agrolavka.Shop;
import ss.entity.agrolavka.Slide;

/**
 * Site data service.
 * @author alex
 */
public interface SiteDataService {
    
    /**
     * Get all shops.
     * @return all shops.
     */
    List<Shop> getAllShops();
    
    /**
     * Get all slides.
     * @return all slides.
     */
    List<Slide> getAllSlides();
}
