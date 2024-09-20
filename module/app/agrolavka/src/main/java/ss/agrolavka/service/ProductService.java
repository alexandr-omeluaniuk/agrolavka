package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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
import ss.entity.images.storage.EntityImage;
import ss.entity.security.EntityAudit_;
import ss.martin.core.dao.CoreDao;

import java.util.*;
import java.util.function.Function;
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

    @Autowired
    private ProductVariantsService productVariantsService;

    @Autowired
    private MySkladIntegrationService mySkladService;

    @Autowired
    private CacheManager cacheManager;
    
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
        final var variantsMap = productVariantsService.getVariantsMap();
        if (product.getExternalId() != null && variantsMap.containsKey(product.getExternalId())) {
            final var variants = new ArrayList<ProductVariant>(variantsMap.get(product.getExternalId()));
            Collections.sort(variants);
            variants.add(0, primaryProductVariant(product));
            return variants;
        } else {
            return Collections.emptyList();
        }
    }

    public List<Product> search(ProductsSearchRequest request) {
        final var products = productDao.search(request);
        products.forEach(product -> product.setVariants(getVariants(product)));
        return products;
    }

    public Product createProduct(Product product) {
        Product mySkladEntity = mySkladService.createProduct(product);
        product.setExternalId(mySkladEntity.getExternalId());
        mySkladService.attachImagesToProduct(product);
        return coreDao.create(product);
    }

    public Product updateProduct(Product product) {
        Product entityFromDB = coreDao.findById(product.getId(), Product.class);
        final var isResetCache = !Objects.equals(entityFromDB.getHideModifications(), product.getHideModifications());
        mySkladService.updateProduct(product);
        final List<EntityImage> actualImages = getActualImages(
            entityFromDB.getImages(), product.getImages());
        entityFromDB.setImages(actualImages);
        entityFromDB = coreDao.update(entityFromDB);
        product.setImages(entityFromDB.getImages());
        mySkladService.removeProductImages(product);
        mySkladService.attachImagesToProduct(product);

        entityFromDB.setName(product.getName());
        entityFromDB.setPrice(product.getPrice());
        entityFromDB.setDescription(product.getDescription());
        entityFromDB.setSeoTitle(product.getSeoTitle());
        entityFromDB.setSeoDescription(product.getSeoDescription());
        entityFromDB.setVideoURL(product.getVideoURL());
        entityFromDB.setInvisible(product.getInvisible());
        entityFromDB.setHideModifications(product.getHideModifications());
        final var updated =  coreDao.update(entityFromDB);
        if (isResetCache) {
            resetProductVariantCache();
        }
        return updated;
    }

    public void deleteProduct(Long id) {
        Product product = coreDao.findById(id, Product.class);

        mySkladService.deleteProduct(product);

        coreDao.delete(id, Product.class);
    }

    private ProductVariant primaryProductVariant(Product product) {
        final var variant = new ProductVariant();
        variant.setCharacteristics(createPrimaryCharacteristic(product.getName()));
        variant.setPrice(product.getPrice());
        variant.setName(ProductVariant.createProductWithModificationsName(product.getName()));
        variant.setExternalId(ProductVariant.PRIMARY_VARIANT);
        return variant;
    }

    private String createPrimaryCharacteristic(String productName) {
        if (productName.contains(" ")) {
            final var parts = productName.split(" ");
            return parts[parts.length - 1];
        } else if (productName.contains(",")) {
            final var parts = productName.split(",");
            return parts[parts.length - 1];
        } else {
            return productName;
        }
    }

    private List<EntityImage> getActualImages(
            final List<EntityImage> imagesDB,
            final List<EntityImage> images
    ) {
        Map<Long, EntityImage> map = imagesDB.stream()
                .collect(Collectors.toMap(EntityImage::getId, Function.identity()));
        final var actualImages = new ArrayList<EntityImage>();
        for (EntityImage image : images) {
            if (image.getData() != null) {
                actualImages.add(image);
            } else if (image.getId() != null && map.containsKey(image.getId())) {
                actualImages.add(map.get(image.getId()));
            }
        }
        return actualImages;
    }

    private void resetProductVariantCache() {
        final var caches = new String[] { CacheKey.PRODUCT_VARIANTS };
        Arrays.stream(caches).forEach(name -> {
            final var cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }
}
