package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.core.dao.CoreDao;

import java.util.List;

@Service
public class AllProductGroupsService {

    @Autowired
    private CoreDao coreDao;

    @Cacheable(CacheKey.PRODUCTS_GROUPS)
    public List<ProductsGroup> getAllGroups() {
        return coreDao.getAll(ProductsGroup.class);
    }
}
