package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.dao.ProductDAO;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductVariant;
import ss.martin.core.dao.CoreDao;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductVariantsService {

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SystemSettingsService systemSettingsService;

    @Cacheable(CacheKey.PRODUCT_VARIANTS)
    public Map<String, List<ProductVariant>> getVariantsMap() {
        final var showAll = systemSettingsService.getCurrentSettings().isShowAllProductVariants();
        final var allVariants = coreDao.getAll(ProductVariant.class);
        final var productIds = allVariants.stream().map(ProductVariant::getParentId).toList();
        final var products = productDAO.getByExternalIds(productIds).stream()
            .collect(Collectors.toMap(Product::getExternalId, Function.identity()));
        return allVariants.stream().filter(v -> {
            if (products.containsKey(v.getParentId())
                && Boolean.TRUE.equals(products.get(v.getParentId()).getHideModifications())) {
                return false;
            }
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
