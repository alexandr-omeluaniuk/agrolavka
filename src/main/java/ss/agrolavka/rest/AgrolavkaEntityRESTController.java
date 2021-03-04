/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ss.martin.platform.rest.EntityRESTController;

/**
 * Agrolavka entity controller.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/protected")
class AgrolavkaEntityRESTController extends EntityRESTController {
    @Override
    protected Class getEntityClass(String name) throws Exception {
        return Class.forName("ss.agrolavka.entity." + name);
    }
}
