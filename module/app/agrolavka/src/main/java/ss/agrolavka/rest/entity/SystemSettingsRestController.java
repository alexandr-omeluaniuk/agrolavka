package ss.agrolavka.rest.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.SystemSettings;
import ss.martin.core.dao.CoreDao;

import java.util.Arrays;

@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/system-settings")
public class SystemSettingsRestController {

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping
    public SystemSettings getSettings() {
        final var settings = coreDao.getAll(SystemSettings.class);
        return settings.isEmpty() ? new SystemSettings() : settings.get(0);
    }

    @PutMapping
    public SystemSettings upsertSettings(
        @RequestBody SystemSettings payload
    ) {
        SystemSettings entity = null;
        if (payload.getId() == null) {
            entity = coreDao.create(payload);
        } else {
            final var fromDb = coreDao.findById(payload.getId(), SystemSettings.class);
            fromDb.setDeliveryConditions(payload.getDeliveryConditions());
            fromDb.setDeliveryOrder(payload.getDeliveryOrder());
            fromDb.setDeliveryPaymentDetails(payload.getDeliveryPaymentDetails());
            fromDb.setShowAllProductVariants(payload.isShowAllProductVariants());
            fromDb.setDiscountAbout(payload.getDiscountAbout());
            fromDb.setDiscountParticipate(payload.getDiscountParticipate());
            fromDb.setDiscountSize(payload.getDiscountSize());
            fromDb.setRegistrationInfo(payload.getRegistrationInfo());
            fromDb.setGuaranteeInfo(payload.getGuaranteeInfo());
            entity = coreDao.update(fromDb);
        }
        resetCaches();
        return entity;
    }

    private void resetCaches() {
        final var caches = new String[] { CacheKey.SYSTEM_SETTINGS, CacheKey.PRODUCT_VARIANTS };
        Arrays.stream(caches).forEach(name -> {
            final var cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }
}
