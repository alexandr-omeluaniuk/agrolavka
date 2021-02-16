/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.model.ProductsGroup;
import ss.agrolavka.service.MySkladIntegrationService;

/**
 * Data updater.
 * @author alex
 */
@Component
class DataUpdater {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(DataUpdater.class);
    /** MySklad integration service. */
    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /**
     * Import MySklad data.
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    protected void importMySkladData() {
        try {
            LOG.info("====================================== MY SKLAD DATA UPDATE ===================================");
            long start = System.currentTimeMillis();
            LOG.info("start authentication...");
            mySkladIntegrationService.authentication();
            LOG.info("authentication completed...");
            List<ProductsGroup> productGroups = mySkladIntegrationService.getProductGroups();
            LOG.info("product groups [" + productGroups.size() + "]");
            LOG.info("update database...");
            
            LOG.info("database updated...");
            LOG.info("time [" + (System.currentTimeMillis() - start) + "] ms");
            LOG.info("===============================================================================================");
        } catch (Exception e) {
            LOG.error("Import MySklad data - fail!", e);
        }
    }
}
