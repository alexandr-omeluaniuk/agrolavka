/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.service.BackupService;
import ss.agrolavka.task.DataUpdater;
import ss.martin.security.api.AlertService;

import java.util.ArrayList;
import java.util.List;

/**
 * My Sklad actions.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/protected/mysklad")
public class DataRESTController {

    private static final Logger LOG = LoggerFactory.getLogger(DataRESTController.class);

    @Autowired
    private AlertService alertService;
    /** Data updater. */
    @Autowired
    private DataUpdater dataUpdater;
    /** Backup service. */
    @Autowired
    private BackupService backupService;
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
    /**
     * Do data backup.
     * @param response HTTP response.
     * @throws Exception error. 
     */
    @PutMapping("/backup")
    public void backup(HttpServletResponse response) throws Exception {
        new Thread(() -> {
            try {
                backupService.createBackup();
            } catch (Exception e) {
                LOG.error("Create backup failed", e);
                alertService.sendAlert("Create backup failed", e);
            }
        }).start();
    }
}
