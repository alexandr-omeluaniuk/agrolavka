package ss.agrolavka.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.ProductService;
import ss.agrolavka.util.UrlProducer;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductVariant;
import ss.entity.agrolavka.Product_;
import ss.entity.security.EntityAudit_;
import ss.martin.core.dao.CoreDao;

/**
 * Product service.
 * @author alex
 */
@Service
class ProductServiceImpl implements ProductService{
    
    private static final int QUICK_SEARCH_PRODUCTS_MAX = 100;
    
    @Autowired
    private ProductDAO productDao;
    
    @Autowired
    private CoreDao coreDao;
    
    @Override
    public ProductsSearchResponse quickSearchProducts(final String searchText) {
        final var request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(QUICK_SEARCH_PRODUCTS_MAX);
        request.setText(searchText);
        request.setOrder("asc");
        request.setOrderBy(Product_.NAME);
        final var products = productDao.search(request);
        products.forEach(product -> {
            product.setBuyPrice(null);
            product.setQuantity(product.getQuantity() != null && product.getQuantity() > 0 ? 1d : 0d);
            product.setUrl(UrlProducer.buildProductUrl(product));
        });
        final var count = productDao.count(request);
        return new ProductsSearchResponse(products, count);
    }
    
    @Override
    @Cacheable(CacheKey.NEW_PRODUCTS)
    public List<Product> getNewProducts() {
        final var searchRequest = new ProductsSearchRequest();
        searchRequest.setPage(1);
        searchRequest.setPageSize(12);
        searchRequest.setOrder("desc");
        searchRequest.setOrderBy(EntityAudit_.CREATED_DATE);
        return productDao.search(searchRequest);
    }

    @Override
    @Cacheable(CacheKey.PRODUCTS_WITH_DISCOUNT)
    public List<Product> getProductsWithDiscount() {
        final var searchRequest = new ProductsSearchRequest();
        searchRequest.setPage(1);
        searchRequest.setPageSize(Integer.MAX_VALUE);
        searchRequest.setWithDiscounts(true);
        final var products = productDao.search(searchRequest).stream()
            .filter(product -> product.getQuantity() != null && product.getQuantity() > 0)
            .collect(Collectors.toList());
        Collections.sort(products, (p1, p2) -> {
            final var id1 = p1.getDiscount().getId();
            final var id2 = p2.getDiscount().getId();
            return id1 > id2 ? -1 : 1;
        });
        return products;
    }

    @Override
    @Cacheable(CacheKey.PRODUCTS_COUNT)
    public Long getProductsCount() {
        final var request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(Integer.MAX_VALUE);
        return productDao.count(request);
    }

    @Override
    public List<ProductVariant> getVariants(String externalId) {
        final var variantsMap = getVariantsMap();
        if (variantsMap.containsKey(externalId)) {
            final var variants = variantsMap.get(externalId);
            Collections.sort(variants);
            return variants;
        } else {
            return Collections.emptyList();
        }
    }
    
    @Cacheable(CacheKey.PRODUCT_VARIANTS)
    private Map<String, List<ProductVariant>> getVariantsMap() {
        return coreDao.getAll(ProductVariant.class).stream().collect(
            Collectors.groupingBy(ProductVariant::getParentId)
        );
    }

    @Override
    public List<Product> search(ProductsSearchRequest request) {
        final var products = productDao.search(request);
        products.forEach(product -> product.setVariants(getVariants(product.getExternalId())));
        return products;
    }
}
