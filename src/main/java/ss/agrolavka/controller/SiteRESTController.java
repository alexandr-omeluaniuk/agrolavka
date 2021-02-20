/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.model.Product;

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
    @RequestMapping(value = "/product-image/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Transactional(propagation = Propagation.SUPPORTS)
    public byte[] list(@PathVariable("id") Long id) throws Exception {
        Product product = coreDAO.findById(id, Product.class);
        return product != null && !product.getImages().isEmpty() ? product.getImages().get(0).getImageData() : null;
    }
}
