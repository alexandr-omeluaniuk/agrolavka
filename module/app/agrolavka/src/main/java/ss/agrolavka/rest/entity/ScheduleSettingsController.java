package ss.agrolavka.rest.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.ScheduleSettings;
import ss.martin.core.dao.CoreDao;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/schedule-settings")
public class ScheduleSettingsController {

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping
    public List<ScheduleSettings> getSettings() {
        return coreDao.getAll(ScheduleSettings.class);
    }

    @PutMapping
    public ScheduleSettings upsertSettings(
        @RequestBody ScheduleSettings payload
    ) {
        ScheduleSettings entity = null;
        if (payload.getId() == null) {
            entity = coreDao.create(payload);
        } else {
            final var fromDb = coreDao.findById(payload.getId(), ScheduleSettings.class);
            fromDb.setDayOfWeek(payload.getDayOfWeek());
            fromDb.setFromDate(payload.getFromDate());
            fromDb.setToDate(payload.getToDate());
            entity = coreDao.update(fromDb);
        }
        resetCaches();
        return entity;
    }

    private void resetCaches() {
        final var caches = new String[] {
            CacheKey.SCHEDULE_SETTINGS,
            CacheKey.PRODUCT_VARIANTS,
            CacheKey.SYSTEM_SETTINGS
        };
        Arrays.stream(caches).forEach(name -> {
            final var cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }
}
