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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.AgrolavkaDAO;
import ss.agrolavka.dao.ExternalEntityDAO;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.entity.Product;
import ss.agrolavka.entity.ProductImage;
import ss.agrolavka.entity.ProductsGroup;
import ss.martin.platform.security.StandardRole;
import ss.martin.platform.spring.security.UserPrincipal;

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
    private AgrolavkaDAO coreDAO;
    /** External entity DAO. */
    @Autowired
    private ExternalEntityDAO externalEntityDAO;
    
    @Autowired
    private AuthenticationManager authManager;
    /**
     * Import MySklad data.
     */
    //@Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    protected void importMySkladData() {
        try {
            GrantedAuthority ga = new SimpleGrantedAuthority(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR.name());
            List<GrantedAuthority> gaList = new ArrayList<>();
            gaList.add(ga);
            Authentication a = authManager.authenticate(new UserPrincipal("starshistrelok@gmail.com", "8228023mts", gaList));
            SecurityContextHolder.getContext().setAuthentication(a);
            LOG.info("====================================== MY SKLAD DATA UPDATE ===================================");
            long start = System.currentTimeMillis();
            LOG.info("start authentication...");
            mySkladIntegrationService.authentication();
            LOG.info("authentication completed...");
            importProductGroups();
            importProducts();
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
    private void importProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        int offset = 0;
        while (offset < 5000) {
            LOG.info("products portion: " + offset + " - " + (offset + 1000));
            List<Product> chunk = mySkladIntegrationService.getProducts(offset, 1000);
            products.addAll(chunk);
            offset += 1000;
        }
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
            eProduct.setGroup(actualProduct.getGroup());
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
    }
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
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
