/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.wrapper.ProductsSearchRequest;
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
    /**
     * Search products.
     * @param page page.
     * @param pageSize page size.
     * @param searchText search text.
     * @param groupId group ID.
     * @return products portion.
     * @throws Exception error.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntitySearchResponse search(
            @RequestParam("page") Integer page,
            @RequestParam("page_size") Integer pageSize,
            @RequestParam(value = "search_text", required = false) String searchText,
            @RequestParam(value = "group_id", required = false) Long groupId)
            throws Exception {
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(page);
        request.setPageSize(pageSize);
        request.setText(searchText);
        request.setGroupId(groupId);
        EntitySearchResponse response = new EntitySearchResponse();
        response.setData(productDAO.search(request));
        response.setTotal(productDAO.count(request).intValue());
        return response;
    }
}
