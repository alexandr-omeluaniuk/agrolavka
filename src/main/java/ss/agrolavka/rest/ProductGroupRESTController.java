/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;

/**
 * Product group REST controller.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/protected/product-group")
public class ProductGroupRESTController {
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /**
     * Get product group image.
     * @param id product group ID.
     * @return image.
     * @throws Exception error.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityImage getProductGroupImage(@PathVariable("id") Long id) throws Exception {
        EntityImage image = coreDAO.findById(id, ProductsGroup.class).getImage();
        return image;
    }
}
