/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.rest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ss.agrolavka.service.BackupService;
import ss.agrolavka.task.DataUpdater;

/**
 * My Sklad actions.
 * @author alex
 */
@RestController
@RequestMapping("/api/agrolavka/protected/mysklad")
public class DataRESTController {
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
    @RequestMapping(value = "/backup", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void backup(HttpServletResponse response) throws Exception {
        File backup = backupService.doBackup();
        response.getOutputStream().write(Files.readAllBytes(Paths.get(backup.toURI())));
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + backup.getName());
        response.addHeader("ContentType", "application/zip");
        response.addHeader("Content-Length", backup.length() + "");
    }
}
