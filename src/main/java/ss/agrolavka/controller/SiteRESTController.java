/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.constants.ImageStubs;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.model.Product;
import ss.agrolavka.wrapper.ProductsSearchRequest;

/**
 * Site REST controller.
 * @author alex
 */
@RestController
@RequestMapping("/api")
public class SiteRESTController {
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    @RequestMapping(value = "/product-image/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Transactional(propagation = Propagation.SUPPORTS)
    public byte[] list(@PathVariable("id") Long id) throws Exception {
        Product product = coreDAO.findById(id, Product.class);
        return product != null && !product.getImages().isEmpty() ? product.getImages().get(0).getImageData()
                : Base64.getDecoder().decode(ImageStubs.NO_PRODUCT_IMAGE);
    }
    @RequestMapping(value = "/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> search(@RequestParam(value = "searchText", required = false) String searchText)
            throws Exception {
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(20);
        request.setText(searchText);
        Map<String, Object> result = new HashMap<>();
        result.put("data", productDAO.search(request));
        result.put("count", productDAO.count(request));
        return result;
    }
}
