/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.GroupProductsService;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.Product;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.wrapper.EntitySearchResponse;

/**
 * Product REST controller.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/protected/product")
public class ProductRESTController {
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Group products service. */
    @Autowired
    private GroupProductsService groupProductsService;
    /**
     * Search products.
     * @param page page.
     * @param pageSize page size.
     * @param order order.
     * @param orderBy order by.
     * @param searchText search text.
     * @param code product code.
     * @param groupId group ID.
     * @param available product is available.
     * @param discounts with discounts.
     * @param includesHidden includes hidden products.
     * @return products portion.
     * @throws Exception error.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntitySearchResponse search(
            @RequestParam("page") Integer page,
            @RequestParam("page_size") Integer pageSize,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "order_by", required = false) String orderBy,
            @RequestParam(value = "search_text", required = false) String searchText,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "group_id", required = false) Long groupId,
            @RequestParam(value = "available", required = false) boolean available,
            @RequestParam(value = "discounts", required = false) boolean discounts,
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
        EntitySearchResponse response = new EntitySearchResponse();
        response.setData(productDAO.search(request));
        response.setTotal(productDAO.count(request).intValue());
        return response;
    }
    
    /**
     * Group products by volume.
     * @throws Exception error.
     */
    @RequestMapping(value = "/group", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void groupProductsByVolume() throws Exception {
        groupProductsService.groupProductByVolumes();
    }
    /**
     * Get product by ID.
     * @param id product ID.
     * @return product or null;
     * @throws Exception error.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProductById(@PathVariable("id") Long id)throws Exception {
        final Product product = coreDAO.findById(id, Product.class);
        return product;
    }
}
