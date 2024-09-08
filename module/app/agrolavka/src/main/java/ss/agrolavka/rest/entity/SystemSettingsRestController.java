package ss.agrolavka.rest.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.SystemSettings;
import ss.martin.core.dao.CoreDao;

@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/system-settings")
public class SystemSettingsRestController {

    @Autowired
    private CoreDao coreDao;

    @GetMapping
    public SystemSettings getSettings() {
        final var settings = coreDao.getAll(SystemSettings.class);
        return settings.isEmpty() ? new SystemSettings() : settings.get(0);
    }

    @PutMapping
    public SystemSettings upsertSettings(
        @RequestBody SystemSettings payload
    ) {
        if (payload.getId() == null) {
            return coreDao.create(payload);
        } else {
            return coreDao.update(payload);
        }
    }
}
