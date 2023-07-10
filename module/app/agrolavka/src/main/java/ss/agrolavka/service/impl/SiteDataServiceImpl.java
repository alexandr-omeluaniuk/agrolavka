package ss.agrolavka.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.service.SiteDataService;
import ss.entity.agrolavka.Shop;
import ss.entity.agrolavka.Slide;
import ss.martin.core.dao.CoreDao;

/**
 * Site data service implementation.
 * @author alex
 */
@Service
class SiteDataServiceImpl implements SiteDataService {
    
    @Autowired
    private CoreDao coreDao;

    @Override
    @Cacheable(CacheKey.SHOPS)
    public List<Shop> getAllShops() {
        return coreDao.getAll(Shop.class);
    }

    @Override
    @Cacheable(CacheKey.SLIDES)
    public List<Slide> getAllSlides() {
        return coreDao.getAll(Slide.class);
    }
}
