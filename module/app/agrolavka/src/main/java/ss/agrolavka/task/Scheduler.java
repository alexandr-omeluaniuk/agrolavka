package ss.agrolavka.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ss.agrolavka.constants.CacheKey;

@Component
public class Scheduler {

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(cron = "0 0 * * * *")
    public void runTask() {
        final var cache = cacheManager.getCache(CacheKey.PURCHASE_HISTORY);
        if (cache != null) {
            cache.clear();
        }
    }
}
