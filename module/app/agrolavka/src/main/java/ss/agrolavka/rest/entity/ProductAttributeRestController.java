package ss.agrolavka.rest.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.dao.ProductAttributeLinkDao;
import ss.entity.agrolavka.ProductAttribute;
import ss.entity.agrolavka.ProductAttributeItem;
import ss.entity.agrolavka.ProductAttributeLink;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/product-attributes")
public class ProductAttributeRestController extends BasicEntityRestController<ProductAttribute> {

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
        return coreDAO.create(attribute);
    }

    @PostMapping("/item/{id}")
    public ProductAttributeItem createItem(
        @PathVariable("id") Long id,
        @RequestBody ProductAttributeItem item
    ) {
        item.setProductAttribute(coreDAO.findById(id, ProductAttribute.class));
        return coreDAO.create(item);
    }

    @PutMapping("/item")
    public ProductAttributeItem editItem(
        @RequestBody ProductAttributeItem item
    ) {
        final var record = coreDAO.findById(item.getId(), ProductAttributeItem.class);
        record.setName(item.getName());
        return coreDAO.update(record);
    }

    @DeleteMapping("/item/{id}")
    public void deleteItem(
        @PathVariable("id") Long id
    ) {
        coreDAO.delete(id, ProductAttributeItem.class);
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
    }
}
