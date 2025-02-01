package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.entity.agrolavka.ScheduleSettings;
import ss.entity.agrolavka.SystemSettings;
import ss.martin.core.dao.CoreDao;

import java.util.List;

@Service
public class SystemSettingsService {

    @Autowired
    private CoreDao coreDao;

    @Cacheable(CacheKey.SYSTEM_SETTINGS)
    public SystemSettings getCurrentSettings() {
        final var settings = coreDao.getAll(SystemSettings.class);
        return settings.isEmpty() ? new SystemSettings() : settings.get(0);
    }

    @Cacheable(CacheKey.SCHEDULE_SETTINGS)
    public List<ScheduleSettings> getScheduleSettings() {
        return coreDao.getAll(ScheduleSettings.class);
    }
}
