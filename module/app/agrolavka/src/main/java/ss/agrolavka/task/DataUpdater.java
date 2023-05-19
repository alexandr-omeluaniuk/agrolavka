/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.task;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.AgrolavkaConfiguration;
import ss.agrolavka.dao.ExternalEntityDAO;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.GroupProductsService;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.util.UrlProducer;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Discount;
import ss.entity.agrolavka.PriceType;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.EntityImage;
import ss.martin.core.dao.CoreDao;
import ss.martin.platform.service.SecurityService;

/**
 * Data updater.
 * @author alex
 */
@Component
public class DataUpdater {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(DataUpdater.class);
    /** MySklad integration service. */
    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    /** External entity DAO. */
    @Autowired
    private ExternalEntityDAO externalEntityDAO;
    /** Security service. */
    @Autowired
    private SecurityService securityService;
    /** Agrolavka configuration. */
    @Autowired
    private AgrolavkaConfiguration configuration;
    /** Group products service. */
    @Autowired
    private GroupProductsService groupProductsService;
    
    @PostConstruct
    protected void init() {
        try {
            AppCache.flushCache(coreDAO.getAll(ProductsGroup.class));
        } catch (Exception ex) {
            LOG.error("Update URL producer catalog - fail!");
        }
    }
    /**
     * Import MySklad data.
     */
    @Scheduled(cron = "0 30 2 * * *")
    public void importMySkladData() {
        int attempts = 0;
        while (attempts < 5) {
            if (doImport()) {
                return;
            } else {
                attempts++;
            }
        }
    }
    
    private boolean doImport() {
        try {
            LOG.info("====================================== MY SKLAD DATA UPDATE ===================================");
            securityService.backgroundAuthentication(
                    configuration.getBackgroundUserUsername(), configuration.getBackgroundUserPassword());
            importPriceTypes();
            importProductGroups();
            importProducts();
            importImages();
            groupProductsService.groupProductByVolumes();
            AppCache.flushCache(coreDAO.getAll(ProductsGroup.class));
            LOG.info("===============================================================================================");
            return true;
        } catch (Exception e) {
            LOG.warn("Import MySklad data - fail!", e);
            return false;
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void importPriceTypes() throws Exception {
        List<PriceType> priceTypes = mySkladIntegrationService.getPriceTypes();
        Map<String, PriceType> map = new HashMap<>();
        for (PriceType pt : priceTypes) {
            map.put(pt.getExternalId(), pt);
        }
        Set<String> actualPriceTypesIDs = map.keySet();
        Set<String> deleteNotIn = new HashSet<>(map.keySet());
        Set<String> existPriceTypesIDs = new HashSet<>();
        List<PriceType> existingPriceTypes = externalEntityDAO.getExternalEntitiesByIds(map.keySet(), PriceType.class);
        for (PriceType existingPriceType : existingPriceTypes) {
            PriceType freshPriceType = map.get(existingPriceType.getExternalId());
            existingPriceType.setName(freshPriceType.getName());
            existPriceTypesIDs.add(existingPriceType.getExternalId());
        }
        LOG.info("update price types [" + existingPriceTypes.size() + "]");
        coreDAO.massUpdate(existingPriceTypes);
        // create new groups
        actualPriceTypesIDs.removeAll(existPriceTypesIDs);
        List<PriceType> newPriceTypes = new ArrayList<>();
        for (String newExternalId : actualPriceTypesIDs) {
            PriceType newPriceType = map.get(newExternalId);
            LOG.info("create new price type: " + newPriceType);
            newPriceTypes.add(newPriceType);
        }
        LOG.info("new price types [" + newPriceTypes.size() + "]");
        coreDAO.massCreate(newPriceTypes);
        // remove unused groups.
        externalEntityDAO.removeExternalEntitiesNotInIDs(deleteNotIn, PriceType.class);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void importProductGroups() throws Exception {
        List<ProductsGroup> productGroups = mySkladIntegrationService.getProductGroups();
        LOG.info("product groups [" + productGroups.size() + "]");
        Map<String, ProductsGroup> groupsMap = new HashMap<>();
        Set<String> unique = new HashSet<>();
        for (ProductsGroup productGroup : productGroups) {
            String t = UrlProducer.transliterate(productGroup.getName());
            while (unique.contains(t)) {
                t += "-alt";
            }
            productGroup.setUrl(t);
            groupsMap.put(productGroup.getExternalId(), productGroup);
            unique.add(t);
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
            eGroup.setUrl(actualGroup.getUrl());
            eGroup.setDescription(actualGroup.getDescription());
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
        productDAO.deleteProductsByNotProductGroupIDs(deleteNotIn);
        externalEntityDAO.removeExternalEntitiesNotInIDs(deleteNotIn, ProductsGroup.class);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void importProducts() throws Exception {
        productDAO.resetDiscounts();
        for (Discount discount : coreDAO.getAll(Discount.class)) {
            coreDAO.delete(discount.getId(), Discount.class);
        }
        List<Discount> discounts = mySkladIntegrationService.getDiscounts();
        Map<String, Discount> discountsMap = new HashMap();
        for (Discount discount : discounts) {
            discount.getProducts().forEach(p -> {
                discountsMap.put(p.getExternalId(), discount);
            });
            discount.setProducts(null);
        }
        coreDAO.massCreate(discounts);
        LOG.info("discounts [" + discounts.size() + "]");
        List<Product> products = new ArrayList<>();
        Map<String, Product> stock = new HashMap<>();
        int offset = 0;
        while (offset < 5000) {
            LOG.info("products portion: " + offset + " - " + (offset + 1000));
            List<Product> chunk = mySkladIntegrationService.getProducts(offset, 1000);
            stock.putAll(mySkladIntegrationService.getStock(offset, 1000));
            products.addAll(chunk);
            offset += 1000;
        }
        LOG.info("products [" + products.size() + "]");
        Set<String> unique = new HashSet<>();
        Map<String, Product> productsMap = new HashMap<>();
        for (Product product : products) {
            String t = UrlProducer.transliterate(product.getName());
            while (unique.contains(t)) {
                t += "-alt";
            }
            product.setUrl(t);
            product.setDiscount(discountsMap.get(product.getExternalId()));
            productsMap.put(product.getExternalId(), product);
            unique.add(t);
        }
        Set<String> actualProductsIDs = productsMap.keySet();
        Set<String> deleteNotIn = new HashSet<>(productsMap.keySet());
        // update existing groups
        List<Product> existProducts = externalEntityDAO.getExternalEntitiesByIds(actualProductsIDs, Product.class);
        Set<String> existProductsIDs = new HashSet<>();
        for (Product eProduct : existProducts) {
            Product actualProduct = productsMap.get(eProduct.getExternalId());
            Product stockProduct = stock.get(actualProduct.getCode());
            eProduct.setName(actualProduct.getName());
            eProduct.setPrice(actualProduct.getPrice());
            eProduct.setGroup(actualProduct.getGroup());
            eProduct.setArticle(actualProduct.getArticle());
            eProduct.setBuyPrice(actualProduct.getBuyPrice());
            eProduct.setDescription(actualProduct.getDescription());
            eProduct.setCode(actualProduct.getCode());
            eProduct.setUrl(actualProduct.getUrl());
            eProduct.setQuantity(stockProduct != null ? stockProduct.getQuantity() : 0);
            eProduct.setUpdated(actualProduct.getUpdated());
            eProduct.setDiscount(actualProduct.getDiscount());
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
        ProductsSearchRequest requestX = new ProductsSearchRequest();
        requestX.setPage(1);
        requestX.setPageSize(Integer.MAX_VALUE);
        AppCache.setProductsCount(productDAO.count(requestX));
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    private void importImages() throws Exception {
        long start = System.currentTimeMillis();
        LOG.info("start images import...");
        LOG.info("");
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        List<Product> products = productDAO.getLastModifiedProducts(calendar.getTime());
        LOG.info("found [" + products.size() + "] products for images uploading");
        int counter = 0;
        for (Product product : products) {
            counter++;
            List<EntityImage> productImages = new ArrayList<>();
            try {
                if (GroupProductsService.GROUPED_PRODUCT_EXTERNAL_ID.equals(product.getExternalId())) {
                    continue;
                }
                List<EntityImage> images = mySkladIntegrationService.getProductImages(product.getExternalId());
                final Set<String> imageKeys = images.stream().map((i) -> i.getSize() + "::" + i.getName())
                    .collect(Collectors.toSet());
                final List<EntityImage> restImages = product.getImages().stream().filter(i -> {
                    return !imageKeys.contains(i.getSize() + "::" + i.getName());
                }).collect(Collectors.toList());
                productImages.addAll(restImages);
                productImages.addAll(images);
                product.setImages(productImages);
                coreDAO.update(product);
            } catch (Exception e) {
                LOG.warn("Can't synchronize product images: " + product, e);
                coreDAO.update(product);
            }
            double progress = ((double) counter / (double) products.size()) * 100;
            if (counter % 100 == 0) {
                LOG.info("progress: " + String.format("%.2f", progress));
            }
        }
        LOG.info("images import completed...");
        LOG.info("elapsed time [" + (System.currentTimeMillis() - start) + "] ms");
    }
   
}