package ss.agrolavka.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.GroupProductsService;
import ss.agrolavka.service.ProductAttributesService;
import ss.agrolavka.service.ProductService;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Product;
import ss.martin.core.dao.CoreDao;
import ss.martin.core.model.EntitySearchResponse;

@RestController
@RequestMapping(SiteUrls.URL_PROTECTED + "/product")
public class ProductRESTController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CoreDao coreDAO;

    @Autowired
    private GroupProductsService groupProductsService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductAttributesService productAttributesService;

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntitySearchResponse<Product> search(
            @RequestParam("page") Integer page,
            @RequestParam("page_size") Integer pageSize,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "order_by", required = false) String orderBy,
            @RequestParam(value = "search_text", required = false) String searchText,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "group_id", required = false) Long groupId,
            @RequestParam(value = "available", required = false) boolean available,
            @RequestParam(value = "discounts", required = false) boolean discounts,
            @RequestParam(value = "invisible", required = false) boolean invisible,
            @RequestParam(value = "includesHidden", required = false) boolean includesHidden
    ) throws Exception {
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(page);
        request.setPageSize(pageSize);
        request.setText(searchText);
        request.setGroupId(groupId);
        request.setOrder(order);
        request.setOrderBy(orderBy);
        request.setCode(code);
        request.setAvailable(available);
        request.setWithDiscounts(discounts);
        request.setIncludesHidden(includesHidden);
        request.setInvisible(invisible);
        final var products = productDAO.search(request);
        productAttributesService.setAttributeLinks(products);
        return new EntitySearchResponse<Product>(productDAO.count(request).intValue(), products);
    }

    @RequestMapping(value = "/group", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void groupProductsByVolume() throws Exception {
        groupProductsService.groupProductByVolumes();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProductById(@PathVariable("id") Long id)throws Exception {
        final Product product = coreDAO.findById(id, Product.class);
        return product;
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping
    public Product update(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }
}
