package ss.agrolavka.rest.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.service.ProductsGroupService;
import ss.entity.agrolavka.ProductsGroup;

/**
 * Product group REST controller.
 * @author alex
 */
@RestController
@RequestMapping(SiteConstants.URL_PROTECTED + "/products-group")
public class ProductGroupRestController extends BasicEntityWithImagesRestController<ProductsGroup> {
    
    @Autowired
    private ProductsGroupService productsGroupService;

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
        return productsGroupService.create(entity);
    }
    
    @PutMapping
    @Override
    public ProductsGroup update(
        @RequestPart(value = "image", required = false) final MultipartFile[] files, 
        @RequestPart(value = "entity", required = true) final ProductsGroup entity
    ) {
        setImages(entity, files);
        return productsGroupService.update(entity);
    }
    
    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable("id") final Long id) {
        productsGroupService.delete(id);
    }
}
