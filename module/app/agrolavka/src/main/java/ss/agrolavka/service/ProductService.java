package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.util.PriceCalculator;
import ss.agrolavka.util.UrlProducer;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductVariant;
import ss.entity.agrolavka.Product_;
import ss.entity.security.EntityAudit_;
import ss.martin.core.dao.CoreDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Product service.
 * @author alex
 */
@Service
public class ProductService {
    
    private static final int QUICK_SEARCH_PRODUCTS_MAX = 100;
    
    @Autowired
    private ProductDAO productDao;
    
    @Autowired
    private CoreDao coreDao;
    
    public ProductsSearchResponse quickSearchProducts(final String searchText) {
        final var request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(QUICK_SEARCH_PRODUCTS_MAX);
        request.setText(searchText);
        request.setOrder("asc");
        request.setOrderBy(Product_.NAME);
        final var products = productDao.search(request);
        products.forEach(product -> {
            product.setPrice(PriceCalculator.getShopPrice(product.getPrice(), product.getDiscount()));
            product.setBuyPrice(null);
            product.setQuantity(product.getQuantity() != null && product.getQuantity() > 0 ? 1d : 0d);
            product.setUrl(UrlProducer.buildProductUrl(product));
            final var variants = getVariants(product);
            if (!variants.isEmpty()) {
                final var lowestVariantPrice = PriceCalculator.getShopPrice(
                        variants.get(variants.size() - 1).getPrice(),
                        product.getDiscount()
                );
                if (lowestVariantPrice < product.getPrice()) {
                    product.setPrice(lowestVariantPrice);
                }
            }
        });
        final var count = productDao.count(request);
        return new ProductsSearchResponse(products, count);
    }
    
    @Cacheable(CacheKey.NEW_PRODUCTS)
    public List<Product> getNewProducts() {
        final var searchRequest = new ProductsSearchRequest();
        searchRequest.setPage(1);
        searchRequest.setPageSize(12);
        searchRequest.setOrder("desc");
        searchRequest.setOrderBy(EntityAudit_.CREATED_DATE);
        final var products =  productDao.search(searchRequest);
        products.forEach(p -> p.setVariants(getVariants(p)));
        return products;
    }

    @Cacheable(CacheKey.PRODUCTS_WITH_DISCOUNT)
    public List<Product> getProductsWithDiscount() {
        final var searchRequest = new ProductsSearchRequest();
        searchRequest.setPage(1);
        searchRequest.setPageSize(Integer.MAX_VALUE);
        searchRequest.setWithDiscounts(true);
        final var rawProducts = productDao.search(searchRequest);
        rawProducts.forEach(p -> p.setVariants(getVariants(p)));
        final var products = rawProducts.stream()
            .filter(product ->
                (product.getQuantity() != null && product.getQuantity() > 0)
                    || !product.getVariants().isEmpty()
            ).collect(Collectors.toList());
        Collections.sort(products, (p1, p2) -> {
            final var id1 = p1.getDiscount().getId();
            final var id2 = p2.getDiscount().getId();
            if (id1 > id2) {
                return -1;
            } else if (id1 < id2) {
                return 1;
            } else {
                return 0;
            }
        });
        return products;
    }

    @Cacheable(CacheKey.PRODUCTS_COUNT)
    public Long getProductsCount() {
        final var request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(Integer.MAX_VALUE);
        return productDao.count(request);
    }

    public List<ProductVariant> getVariants(Product product) {
        final var variantsMap = getVariantsMap();
        if (product.getExternalId() != null && variantsMap.containsKey(product.getExternalId())) {
            final var variants = variantsMap.get(product.getExternalId());
            Collections.sort(variants);
            variants.add(0,primaryProductVariant(product));
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

    public List<Product> search(ProductsSearchRequest request) {
        final var products = productDao.search(request);
        products.forEach(product -> product.setVariants(getVariants(product)));
        return products;
    }

    private ProductVariant primaryProductVariant(Product product) {
        final var variant = new ProductVariant();
        variant.setCharacteristics(createPrimaryCharacteristic(product.getName()));
        variant.setPrice(product.getPrice());
        variant.setName(product.getName());
        variant.setExternalId(ProductVariant.PRIMARY_VARIANT);
        return variant;
    }

    private String createPrimaryCharacteristic(String productName) {
        if (productName.contains(",")) {
            final var parts = productName.split(",");
            return parts[parts.length - 1];
        } else if (productName.contains(" ")) {
            final var parts = productName.split(" ");
            return parts[parts.length - 1];
        } else {
            return productName;
        }
    }
}
