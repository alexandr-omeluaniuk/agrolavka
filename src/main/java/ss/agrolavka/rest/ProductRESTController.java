/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.entity.Product;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.entity.EntityImage;
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
    /**
     * Search products.
     * @param page page.
     * @param pageSize page size.
     * @param order order.
     * @param orderBy order by.
     * @param searchText search text.
     * @param groupId group ID.
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
            @RequestParam(value = "group_id", required = false) Long groupId)
            throws Exception {
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(page);
        request.setPageSize(pageSize);
        request.setText(searchText);
        request.setGroupId(groupId);
        request.setOrder(order);
        request.setOrderBy(orderBy);
        EntitySearchResponse response = new EntitySearchResponse();
        response.setData(productDAO.search(request));
        response.setTotal(productDAO.count(request).intValue());
        return response;
    }
    /**
     * Get product images.
     * @param id product ID.
     * @return list of product images.
     * @throws Exception error. 
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EntityImage> getProductImages(@PathVariable("id") Long id) throws Exception {
        List<EntityImage> images = coreDAO.findById(id, Product.class).getImages();
        int size = images.size();
        return images;
    }
}
