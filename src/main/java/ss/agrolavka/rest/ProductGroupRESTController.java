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
import org.springframework.web.bind.annotation.RestController;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.service.EntityService;

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
    /** Entity service. */
    @Autowired
    private EntityService entityService;
    /**
     * Get product group image.
     * @param id product group ID.
     * @return image.
     * @throws Exception error.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EntityImage> getProductGroupImage(@PathVariable("id") Long id) throws Exception {
        List<EntityImage> images = coreDAO.findById(id, ProductsGroup.class).getImages();
        int size = images.size();
        return images;
    }
    /**
     * Normalize product groups.
     * @throws Exception error.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/normalize", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void normalizeProductGroups() throws Exception {
        for (ProductsGroup group : coreDAO.getAll(ProductsGroup.class)) {
            entityService.update(group);
        }
    }
}
