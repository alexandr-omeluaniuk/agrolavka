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

    @Cacheable(CacheKey.PRODUCT_VARIANTS)
    public Map<String, List<ProductVariant>> getVariantsMap() {
        return coreDao.getAll(ProductVariant.class).stream().filter(v -> !Boolean.TRUE.equals(v.getHidden())).collect(
            Collectors.groupingBy(ProductVariant::getParentId)
        );
    }
}
