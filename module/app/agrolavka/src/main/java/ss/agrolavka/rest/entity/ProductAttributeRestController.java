package ss.agrolavka.rest.entity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.SiteUrls;
import ss.entity.agrolavka.ProductAttribute;

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
}
