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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.core.dao.CoreDao;
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
    private CoreDao coreDAO;
    /** Entity service. */
    @Autowired
    private EntityService entityService;
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
