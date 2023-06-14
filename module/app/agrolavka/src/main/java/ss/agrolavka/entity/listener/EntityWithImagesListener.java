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
import ss.entity.images.storage.EntityImage;

/**
 *
 * @author alex
 */
abstract class EntityWithImagesListener {
    
    protected List<EntityImage> getActualImages(
            final List<EntityImage> imagesDB,
            final List<EntityImage> images,
            final int thumbSize
    ) {
        Map<Long, EntityImage> map = imagesDB.stream()
                .collect(Collectors.toMap(EntityImage::getId, Function.identity()));
        final var actualImages = new ArrayList<EntityImage>();
        for (EntityImage image : images) {
            if (image.getData() != null) {
                actualImages.add(image);
            } else if (image.getId() != null && map.containsKey(image.getId())) {
                actualImages.add(map.get(image.getId()));
            }
        }
        return actualImages;
    }
    
}
