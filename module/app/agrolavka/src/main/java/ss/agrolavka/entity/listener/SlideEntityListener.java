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

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ss.agrolavka.util.AppCache;
import ss.entity.agrolavka.Slide;
import ss.martin.core.dao.CoreDao;
import ss.martin.platform.util.PlatformEntityListener;

/**
 *
 * @author alex
 */
@Component
@Qualifier("SlideEntityListener")
class SlideEntityListener implements PlatformEntityListener<Slide> {
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;

    @Override
    public Class<Slide> entity() {
        return Slide.class;
    }
    
    @Override
    public void postPersist(final Slide entity) {
        AppCache.flushSlidesCache(coreDAO.getAll(Slide.class));
    }
    
    @Override
    public void postUpdate(final Slide entity) {
        AppCache.flushSlidesCache(coreDAO.getAll(Slide.class));
    }
    
    @Override
    public void postDelete(Set<Long> ids) {
        AppCache.flushSlidesCache(coreDAO.getAll(Slide.class));
    }
}
