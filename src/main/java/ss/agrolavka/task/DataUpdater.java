/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.dao.ExternalEntityDAO;
import ss.agrolavka.model.Product;
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
    /** External entity DAO. */
    @Autowired
    private ExternalEntityDAO externalEntityDAO;
    /**
     * Import MySklad data.
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    protected void importMySkladData() {
        try {
            LOG.info("====================================== MY SKLAD DATA UPDATE ===================================");
            long start = System.currentTimeMillis();
            LOG.info("start authentication...");
            mySkladIntegrationService.authentication();
            LOG.info("authentication completed...");
            importProductGroups();
            importProducts();
            LOG.info("time [" + (System.currentTimeMillis() - start) + "] ms");
            LOG.info("===============================================================================================");
        } catch (Exception e) {
            LOG.error("Import MySklad data - fail!", e);
        }
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void importProductGroups() throws Exception {
        List<ProductsGroup> productGroups = mySkladIntegrationService.getProductGroups();
        LOG.info("product groups [" + productGroups.size() + "]");
        Map<String, ProductsGroup> groupsMap = new HashMap<>();
        Set<String> actualGroupIDs = groupsMap.keySet();
        for (ProductsGroup productGroup : productGroups) {
            groupsMap.put(productGroup.getExternalId(), productGroup);
        }
        // update existing groups
        List<ProductsGroup> existGroups = externalEntityDAO.getExternalEntitiesByIds(
                actualGroupIDs, ProductsGroup.class);
        Set<String> existGroupsIDs = new HashSet<>();
        for (ProductsGroup eGroup : existGroups) {
            ProductsGroup actualGroup = groupsMap.get(eGroup.getExternalId());
            eGroup.setName(actualGroup.getName());
            eGroup.setParentId(actualGroup.getParentId());
            existGroupsIDs.add(eGroup.getExternalId());
        }
        coreDAO.massUpdate(existGroups);
        // create new groups
        actualGroupIDs.removeAll(existGroupsIDs);
        List<ProductsGroup> newGroups = new ArrayList<>();
        for (String newGroupExternalId : actualGroupIDs) {
            ProductsGroup newProductGroup = groupsMap.get(newGroupExternalId);
            LOG.info("create new group: " + newProductGroup);
            newGroups.add(newProductGroup);
        }
        coreDAO.massCreate(newGroups);
        // remove unused groups.
        externalEntityDAO.removeExternalEntitiesNotInIDs(groupsMap.keySet(), ProductsGroup.class);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void importProducts() throws Exception {
        List<Product> products = mySkladIntegrationService.getProducts();
        LOG.info("products [" + products.size() + "]");
        Map<String, Product> productsMap = new HashMap<>();
        Set<String> actualProductsIDs = productsMap.keySet();
        for (Product product : products) {
            productsMap.put(product.getExternalId(), product);
        }
        // update existing groups
        List<Product> existProducts = externalEntityDAO.getExternalEntitiesByIds(actualProductsIDs, Product.class);
        Set<String> existProductsIDs = new HashSet<>();
        for (Product eProduct : existProducts) {
            Product actualProduct = productsMap.get(eProduct.getExternalId());
            eProduct.setName(actualProduct.getName());
            eProduct.setPrice(actualProduct.getPrice());
            existProductsIDs.add(eProduct.getExternalId());
        }
        coreDAO.massUpdate(existProducts);
        // create new groups
        actualProductsIDs.removeAll(existProductsIDs);
        List<Product> newProducts = new ArrayList<>();
        for (String newProductExternalId : actualProductsIDs) {
            Product newProduct = productsMap.get(newProductExternalId);
            LOG.info("create product: " + newProduct);
            newProducts.add(newProduct);
        }
        coreDAO.massCreate(newProducts);
        // remove unused groups.
        externalEntityDAO.removeExternalEntitiesNotInIDs(productsMap.keySet(), Product.class);
    }
}
