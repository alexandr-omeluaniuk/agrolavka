package ss.agrolavka.entity.listener;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.util.AppCache;
import ss.entity.agrolavka.Shop;
import ss.entity.images.storage.EntityImage;
import ss.martin.core.dao.CoreDao;
import ss.martin.platform.util.PlatformEntityListener;

/**
 * Shop entity listener
 * @author alex
 */
@Component
@Qualifier("ShopEntityListener")
class ShopEntityListener extends EntityWithImagesListener implements PlatformEntityListener<Shop> {
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;

    @Override
    public Class<Shop> entity() {
        return Shop.class;
    }
    
    @Override
    public void prePersist(final Shop entity) {
        //cropImages(entity.getImages(), SiteConstants.IMAGE_SHOP_THUMB_SIZE);
    }
    
    @Override
    public void postPersist(final Shop entity) {
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }

    @Override
    public void preUpdate(final Shop entity) {
        Shop entityFromDB = coreDAO.findById(entity.getId(), Shop.class);
        final List<EntityImage> actualImages = getActualImages(
                entityFromDB.getImages(), entity.getImages(), SiteConstants.IMAGE_SHOP_THUMB_SIZE);
        entityFromDB.setImages(actualImages);
        entity.setImages(actualImages);
        coreDAO.update(entityFromDB);
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }
    
    @Override
    public void postUpdate(final Shop entity) {
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }
    
    @Override
    public void postDelete(Set<Long> ids) {
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }
    
}
