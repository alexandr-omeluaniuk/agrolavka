package ss.agrolavka.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.ProductService;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.util.UrlProducer;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.Product_;
import ss.entity.security.EntityAudit_;

/**
 * Product service.
 * @author alex
 */
@Service
class ProductServiceImpl implements ProductService{
    
    private static final int QUICK_SEARCH_PRODUCTS_MAX = 100;
    
    @Autowired
    private ProductDAO productDao;
    
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
}
