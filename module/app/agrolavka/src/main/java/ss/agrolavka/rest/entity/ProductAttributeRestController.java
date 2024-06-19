package ss.agrolavka.rest.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.dao.ProductAttributeLinkDao;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.ProductAttribute;
import ss.entity.agrolavka.ProductAttributeItem;
import ss.entity.agrolavka.ProductAttributeLink;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/product-attributes")
public class ProductAttributeRestController extends BasicEntityRestController<ProductAttribute> {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ProductAttributeLinkDao productAttributeLinkDao;

    @Override
    protected Class<ProductAttribute> entityClass() {
        return ProductAttribute.class;
    }

    @PostMapping
    public ProductAttribute create(
        @RequestBody ProductAttribute attribute
    ) {
        attribute.setUrl(UrlProducer.transliterate(attribute.getName()));
        final var result = coreDAO.create(attribute);
        resetAttributesCache();
        return result;
    }

    @PutMapping
    public ProductAttribute edit(
        @RequestBody ProductAttribute attribute
    ) {
        attribute.setUrl(UrlProducer.transliterate(attribute.getName()));
        final var result = coreDAO.update(attribute);
        resetAttributesCache();
        return result;
    }

    @PostMapping("/item/{id}")
    public ProductAttributeItem createItem(
        @PathVariable("id") Long id,
        @RequestBody ProductAttributeItem item
    ) {
        final var attribute = coreDAO.findById(id, ProductAttribute.class);
        item.setProductAttribute(attribute);
        item.setUrl(UrlProducer.transliterate(item.getName()));
        final var result = coreDAO.create(item);
        resetAttributesCache();
        return result;
    }

    @PutMapping("/item")
    public ProductAttributeItem editItem(
        @RequestBody ProductAttributeItem item
    ) {
        final var record = coreDAO.findById(item.getId(), ProductAttributeItem.class);
        record.setName(item.getName());
        record.setUrl(UrlProducer.transliterate(item.getName()));
        final var result = coreDAO.update(record);
        resetAttributesCache();
        return result;
    }

    @DeleteMapping("/item/{id}")
    public void deleteItem(
        @PathVariable("id") Long id
    ) {
        coreDAO.delete(id, ProductAttributeItem.class);
        resetAttributesCache();
    }

    @GetMapping("/links/{id}")
    public List<ProductAttributeLink> getProductAttributes(
        @PathVariable("id") Long productId
    ) {
        return productAttributeLinkDao.getProductLinks(productId);
    }

    @PutMapping("/links/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveProductAttributeLinks(
        @PathVariable("id") Long productId,
        @RequestBody Set<Long> attributeItems
    ) {
        productAttributeLinkDao.deleteProductLinks(productId);
        final var links = coreDAO.findByIds(attributeItems, ProductAttributeItem.class).stream().map(item -> {
            final var link = new ProductAttributeLink();
            link.setAttributeItem(item);
            link.setProductId(productId);
            return link;
        }).toList();
        coreDAO.massCreate(links);
        resetAttributesCache();
    }

    private void resetAttributesCache() {
        final var cache = cacheManager.getCache(CacheKey.PRODUCT_ATTRIBUTE_LINKS);
        if (cache != null) {
            cache.clear();
        }
    }
}
