package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.entity.agrolavka.ProductVariant;
import ss.martin.core.dao.CoreDao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductVariantsService {

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private SystemSettingsService systemSettingsService;

    @Cacheable(CacheKey.PRODUCT_VARIANTS)
    public Map<String, List<ProductVariant>> getVariantsMap() {
        final var showAll = systemSettingsService.getCurrentSettings().isShowAllProductVariants();
        return coreDao.getAll(ProductVariant.class).stream().filter(v -> {
            if (showAll) {
                return true;
            } else {
                return !Boolean.TRUE.equals(v.getHidden());
            }
        }).collect(
            Collectors.groupingBy(ProductVariant::getParentId)
        );
    }
}
