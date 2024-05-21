package ss.agrolavka.rest.entity;

import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.ProductAttribute;
import ss.entity.agrolavka.ProductAttributeItem;

@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/product-attributes")
public class ProductAttributeRestController extends BasicEntityRestController<ProductAttribute> {

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

    @DeleteMapping("/item/{id}")
    public void deleteItem(
        @PathVariable("id") Long id
    ) {
        coreDAO.delete(id, ProductAttributeItem.class);
    }
}
