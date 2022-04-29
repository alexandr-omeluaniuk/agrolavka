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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ss.agrolavka.constants.SiteConstants;
import ss.entity.agrolavka.Shop;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.service.ImageService;
import ss.martin.platform.util.PlatformEntityListener;

/**
 * Shop entity listener
 * @author alex
 */
@Component
@Qualifier("ShopEntityListener")
class ShopEntityListener implements PlatformEntityListener<Shop> {
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Image service. */
    @Autowired
    private ImageService imageService;

    @Override
    public Class<Shop> entity() {
        return Shop.class;
    }
    
    @Override
    public void prePersist(final Shop entity) throws Exception {
        if (entity.getImages() != null) {
            for (EntityImage image : entity.getImages()) {
                byte[] thumb = imageService.convertToThumbnail(image.getData(), SiteConstants.IMAGE_THUMB_SIZE);
                image.setData(thumb);
            }
        }
        if (entity.getMainImage() != null) {
            byte[] thumb = imageService.convertToThumbnail(entity.getMainImage().getData(), SiteConstants.IMAGE_THUMB_SIZE);
            entity.getMainImage().setData(thumb);
        }
    }

    @Override
    public void preUpdate(final Shop entity) throws Exception {
        Shop entityFromDB = coreDAO.findById(entity.getId(), Shop.class);
        List<EntityImage> actualImages = getActualImages(entityFromDB.getImages(), entity.getImages());
        entityFromDB.setImages(actualImages);
        entity.setImages(actualImages);
        List<EntityImage> actualMainImages = getActualImages(
                Collections.singletonList(entityFromDB.getMainImage()),
                Collections.singletonList(entity.getMainImage())
        );
        EntityImage actualMainImage = actualMainImages.isEmpty() ? null : actualMainImages.get(0);
        entityFromDB.setMainImage(actualMainImage);
        entity.setMainImage(actualMainImage);
        coreDAO.update(entityFromDB);
    }
    
    private List<EntityImage> getActualImages(List<EntityImage> imagesDB, List<EntityImage> images) throws Exception {
        Map<Long, EntityImage> map = imagesDB.stream()
                .collect(Collectors.toMap(EntityImage::getId, Function.identity()));
        List<EntityImage> actualImages = new ArrayList();
        for (EntityImage image : images) {
            if (image.getData() != null) {
                byte[] thumb = imageService.convertToThumbnail(image.getData(), SiteConstants.IMAGE_THUMB_SIZE);
                image.setData(thumb);
                actualImages.add(image);
            } else if (image.getId() != null && map.containsKey(image.getId())) {
                actualImages.add(map.get(image.getId()));
            }
        }
        return actualImages;
    }
    
}
