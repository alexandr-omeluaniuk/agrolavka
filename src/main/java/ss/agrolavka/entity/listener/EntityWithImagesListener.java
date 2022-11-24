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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import ss.entity.martin.EntityImage;
import ss.martin.platform.service.ImageService;

/**
 *
 * @author alex
 */
abstract class EntityWithImagesListener {
    
    /** Image service. */
    @Autowired
    private ImageService imageService;
    
    protected void cropImages(final List<EntityImage> images, final int thumbSize) throws Exception {
        for (EntityImage image : images) {
            byte[] thumb = imageService.convertToThumbnail(image.getData(), thumbSize);
            image.setData(thumb);
        }
    }
    
    protected List<EntityImage> getActualImages(
            final List<EntityImage> imagesDB,
            final List<EntityImage> images,
            final int thumbSize
    ) throws Exception {
        Map<Long, EntityImage> map = imagesDB.stream()
                .collect(Collectors.toMap(EntityImage::getId, Function.identity()));
        List<EntityImage> actualImages = new ArrayList();
        for (EntityImage image : images) {
            if (image.getData() != null) {
                byte[] thumb = imageService.convertToThumbnail(image.getData(), thumbSize);
                image.setData(thumb);
                actualImages.add(image);
            } else if (image.getId() != null && map.containsKey(image.getId())) {
                actualImages.add(map.get(image.getId()));
            }
        }
        return actualImages;
    }
    
}
