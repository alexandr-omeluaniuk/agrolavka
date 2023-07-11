package ss.agrolavka.rest.entity;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.ProductsGroup;

/**
 * Product group REST controller.
 * @author alex
 */
@RestController
@RequestMapping(SiteConstants.URL_PROTECTED + "/products-group")
public class ProductGroupRestController extends BasicEntityWithImagesRestController<ProductsGroup> {
    
    @Autowired
    private MySkladIntegrationService mySklad;

    @Override
    protected Class<ProductsGroup> entityClass() {
        return ProductsGroup.class;
    }
    
    @PostMapping
    @Override
    public ProductsGroup create(
        @RequestPart(value = "image", required = false) final MultipartFile[] files, 
        @RequestPart(value = "entity", required = true) final ProductsGroup entity
    ) {
        setImages(entity, files);
        entity.setUrl(UrlProducer.transliterate(entity.getName()));
        entity.setExternalId(mySklad.createProductsGroup(entity));
        final var newEntity = coreDAO.create(entity);
        resetCache();
        return newEntity;
    }
    
    @PutMapping
    @Override
    public ProductsGroup update(
        @RequestPart(value = "image", required = false) final MultipartFile[] files, 
        @RequestPart(value = "entity", required = true) final ProductsGroup entity
    ) {
        setImages(entity, files);
        entity.setUrl(UrlProducer.transliterate(entity.getName()));
        mySklad.updateProductsGroup(entity);
        final var updatedEntity = coreDAO.update(entity);
        resetCache();
        return updatedEntity;
    }
    
    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable("id") final Long id) {
        final var group = coreDAO.findById(id, ProductsGroup.class);
        Optional.ofNullable(group).ifPresent(entity -> {
            mySklad.deleteProductsGroup(group);
            coreDAO.delete(id, entityClass());
            resetCache();
        });
    }
    
    private void resetCache() {
        Optional.ofNullable(cacheManager.getCache(CacheKey.PRODUCTS_GROUPS)).ifPresent(cache -> cache.clear());
    }
}
