/*
 * Copyright (C) 2022 alex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ss.agrolavka.entity.listener;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.util.AppCache;
import ss.entity.agrolavka.Shop;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;
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
    private CoreDAO coreDAO;

    @Override
    public Class<Shop> entity() {
        return Shop.class;
    }
    
    @Override
    public void prePersist(final Shop entity) throws Exception {
        cropImages(entity.getImages(), SiteConstants.IMAGE_SHOP_THUMB_SIZE);
        
    }
    
    @Override
    public void postPersist(final Shop entity) throws Exception {
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }

    @Override
    public void preUpdate(final Shop entity) throws Exception {
        Shop entityFromDB = coreDAO.findById(entity.getId(), Shop.class);
        final List<EntityImage> actualImages = getActualImages(
                entityFromDB.getImages(), entity.getImages(), SiteConstants.IMAGE_SHOP_THUMB_SIZE);
        entityFromDB.setImages(actualImages);
        entity.setImages(actualImages);
        coreDAO.update(entityFromDB);
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }
    
    @Override
    public void postUpdate(final Shop entity) throws Exception {
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }
    
    @Override
    public void postDelete(Set<Long> ids) throws Exception {
        AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
    }
    
}
