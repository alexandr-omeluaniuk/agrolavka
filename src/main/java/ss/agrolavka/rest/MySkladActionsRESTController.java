/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.task.DataUpdater;

/**
 * My Sklad actions.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/protected/mysklad")
public class MySkladActionsRESTController {
    /** Data updater. */
    @Autowired
    private DataUpdater dataUpdater;
    /**
     * Synchronize MySklad data.
     * @return empty array.
     * @throws Exception error.
     */
    @RequestMapping(value = "/synchronize", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public List synchronizeData() throws Exception {
        dataUpdater.importMySkladData();
        return new ArrayList();
    }
}
