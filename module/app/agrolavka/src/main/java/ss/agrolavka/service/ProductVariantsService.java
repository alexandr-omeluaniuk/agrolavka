package ss.agrolavka.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.dao.ProductDAO;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductVariant;
import ss.martin.core.dao.CoreDao;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductVariantsService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductVariantsService.class);

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SystemSettingsService systemSettingsService;

    @Autowired
    private CacheManager cacheManager;

    @PostConstruct
    private void init() {
        final var prohibitedThread = new Thread(() -> {
            Boolean prevProhibitedFlag = null;
            while (true) {
                final var isProhibited = isTimeForShowProhibited();
                if (!Objects.equals(isProhibited, prevProhibitedFlag)) {
                    resetCaches();
                    LOG.info("Prohibited flag resets caches");
                }
                prevProhibitedFlag = isProhibited;
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(30));
                } catch (InterruptedException e) {
                    LOG.warn("Stop thread failed", e);
                }
            }
        });
        prohibitedThread.setName("prohibited-schedule");
        prohibitedThread.start();
    }

    @Cacheable(CacheKey.PRODUCT_VARIANTS)
    public Map<String, List<ProductVariant>> getVariantsMap() {
        final var showAll = systemSettingsService.getCurrentSettings().isShowAllProductVariants();
        final var allVariants = coreDao.getAll(ProductVariant.class);
        final var productIds = allVariants.stream().map(ProductVariant::getParentId).toList();
        final var products = productDAO.getByExternalIds(productIds).stream()
            .collect(Collectors.toMap(Product::getExternalId, Function.identity()));
        final var isProhibited = isTimeForShowProhibited();
        LOG.info("Prohibited flag: " + isProhibited);
        return allVariants.stream().filter(v -> {
            if (products.containsKey(v.getParentId())
                && Boolean.TRUE.equals(products.get(v.getParentId()).getHideModifications())) {
                return false;
            }
            if (showAll) {
                return !isProhibited;
            } else {
                return !Boolean.TRUE.equals(v.getHidden());
            }
        }).collect(
            Collectors.groupingBy(ProductVariant::getParentId)
        );
    }

    private Boolean isTimeForShowProhibited() {
        final var settings = systemSettingsService.getScheduleSettings();
        final var now = LocalDateTime.now().atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.of("Europe/Minsk"));
        final var nowDayOfWeek = now.getDayOfWeek();
        final var nowHours = now.getHour();
        final var nowMinutes = now.getMinute();
        final var nowTimestamp = nowHours * 60 + nowMinutes;
        final var matched = settings.stream().filter(s -> {
            final var fromTimestamp = s.getFromHours() * 60 + s.getFromMinutes();
            final var toTimestamp = s.getToHours() * 60 + s.getToMinutes();
            return DayOfWeek.of(s.getDayOfWeek()) == nowDayOfWeek &&
                fromTimestamp < nowTimestamp && nowTimestamp < toTimestamp;
        }).toList();
        return !matched.isEmpty();
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
