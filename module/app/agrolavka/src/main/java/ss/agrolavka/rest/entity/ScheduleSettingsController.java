package ss.agrolavka.rest.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.ScheduleSettings;
import ss.martin.core.dao.CoreDao;

import java.util.Arrays;

@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/schedule-settings")
public class ScheduleSettingsController extends BasicEntityRestController<ScheduleSettings> {

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private CacheManager cacheManager;

    @PostMapping
    public ScheduleSettings save(
        @RequestBody ScheduleSettings payload
    ) {
        final var entity = coreDao.create(payload);
        resetCaches();
        return entity;
    }

    @PutMapping
    public ScheduleSettings upsertSettings(
        @RequestBody ScheduleSettings payload
    ) {
        final var fromDb = coreDao.findById(payload.getId(), ScheduleSettings.class);
        fromDb.setDayOfWeek(payload.getDayOfWeek());
        fromDb.setFromHours(payload.getFromHours());
        fromDb.setFromMinutes(payload.getFromMinutes());
        fromDb.setToHours(payload.getToHours());
        fromDb.setToMinutes(payload.getToMinutes());
        final var entity = coreDao.update(fromDb);
        resetCaches();
        return entity;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        coreDao.delete(id, ScheduleSettings.class);
        resetCaches();
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

    @Override
    protected Class<ScheduleSettings> entityClass() {
        return ScheduleSettings.class;
    }
}
