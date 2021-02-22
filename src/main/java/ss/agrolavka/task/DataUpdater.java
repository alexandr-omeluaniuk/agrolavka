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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.dao.ExternalEntityDAO;
import ss.agrolavka.model.Product;
import ss.agrolavka.model.ProductImage;
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
    //@Scheduled(fixedRate = 1000 * 60 * 30)
    protected void importMySkladData() {
        try {
            LOG.info("====================================== MY SKLAD DATA UPDATE ===================================");
            long start = System.currentTimeMillis();
            LOG.info("start authentication...");
            mySkladIntegrationService.authentication();
            LOG.info("authentication completed...");
            importProductGroups();
            int portion = 0;
            while (true) {
                LOG.info("products portion: " + portion + " - " + (portion + 1000));
                int productsLoaded = importProducts(portion);
                if (productsLoaded != 1000) {
                    break;
                } else {
                    portion += 1000;
                }
            }
            importImages();
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
        for (ProductsGroup productGroup : productGroups) {
            groupsMap.put(productGroup.getExternalId(), productGroup);
        }
        Set<String> actualGroupIDs = groupsMap.keySet();
        Set<String> deleteNotIn = new HashSet<>(groupsMap.keySet());
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
        LOG.info("update product groups [" + existGroups.size() + "]");
        coreDAO.massUpdate(existGroups);
        // create new groups
        actualGroupIDs.removeAll(existGroupsIDs);
        List<ProductsGroup> newGroups = new ArrayList<>();
        for (String newGroupExternalId : actualGroupIDs) {
            ProductsGroup newProductGroup = groupsMap.get(newGroupExternalId);
            LOG.info("create new group: " + newProductGroup);
            newGroups.add(newProductGroup);
        }
        LOG.info("new product groups [" + newGroups.size() + "]");
        coreDAO.massCreate(newGroups);
        // remove unused groups.
        externalEntityDAO.removeExternalEntitiesNotInIDs(deleteNotIn, ProductsGroup.class);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private int importProducts(int offset) throws Exception {
        List<Product> products = mySkladIntegrationService.getProducts(offset, 1000);
        LOG.info("products [" + products.size() + "]");
        Map<String, Product> productsMap = new HashMap<>();
        for (Product product : products) {
            productsMap.put(product.getExternalId(), product);
        }
        Set<String> actualProductsIDs = productsMap.keySet();
        Set<String> deleteNotIn = new HashSet<>(productsMap.keySet());
        // update existing groups
        List<Product> existProducts = externalEntityDAO.getExternalEntitiesByIds(actualProductsIDs, Product.class);
        Set<String> existProductsIDs = new HashSet<>();
        for (Product eProduct : existProducts) {
            Product actualProduct = productsMap.get(eProduct.getExternalId());
            eProduct.setName(actualProduct.getName());
            eProduct.setPrice(actualProduct.getPrice());
            existProductsIDs.add(eProduct.getExternalId());
            LOG.trace("update product: " + eProduct);
        }
        LOG.info("update products [" + existProducts.size() + "]");
        coreDAO.massUpdate(existProducts);
        // create new groups
        actualProductsIDs.removeAll(existProductsIDs);
        List<Product> newProducts = new ArrayList<>();
        for (String newProductExternalId : actualProductsIDs) {
            Product newProduct = productsMap.get(newProductExternalId);
            LOG.info("create product: " + newProduct);
            newProducts.add(newProduct);
        }
        LOG.info("new products [" + newProducts.size() + "] ");
        coreDAO.massCreate(newProducts);
        // remove unused groups.
        externalEntityDAO.removeExternalEntitiesNotInIDs(deleteNotIn, Product.class);
        return products.size();
    }
    private void importImages() throws Exception {
        List<Product> products = coreDAO.getAll(Product.class);
        for (Product product : products) {
            try {
                List<ProductImage> images = mySkladIntegrationService.getProductImages(product.getExternalId());
                for (ProductImage image : images) {
                    image.setProduct(product);
                }
                product.setImages(images);
                coreDAO.update(product);
            } catch (Exception e) {
                LOG.warn("Can't synchronize product images: " + product, e);
            }
        }
    }
}
