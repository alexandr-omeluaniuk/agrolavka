package ss.agrolavka.task;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.AgrolavkaConfiguration;
import ss.agrolavka.dao.ExternalEntityDAO;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.lucene.LuceneIndexer;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.service.ProductsGroupService;
import ss.agrolavka.task.mysklad.DiscountsSynchronizer;
import ss.agrolavka.task.mysklad.PriceTypeSynchronizator;
import ss.agrolavka.task.mysklad.ProductVariantSynchronizator;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.Discount;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.images.storage.EntityImage;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.api.AlertService;
import ss.martin.security.service.SecurityService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private AlertService alertService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private ProductVariantSynchronizator productVariantSynchronizator;
    
    @Autowired
    private PriceTypeSynchronizator priceTypeSynchronizator;

    @Autowired
    private DiscountsSynchronizer discountsSynchronizer;

    @Autowired
    private ProductsGroupService productsGroupService;

    @Autowired
    private LuceneIndexer indexer;
    
    @PostConstruct
    protected void init() {
        AppCache.flushCache(productsGroupService.getActiveProductGroups());
    }
    /**
     * Import MySklad data.
     */
    @Scheduled(cron = "0 30 2 * * *")
    public void importMySkladData() {
        int attempts = 0;
        while (attempts < 3) {
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
                configuration.backgroundUserUsername(),
                configuration.backgroundUserPassword()
            );
            priceTypeSynchronizator.doImport();
            final var productDiscountMap = discountsSynchronizer.doImport();
            importProductGroups();
            importProducts(productDiscountMap);
            importImages();
            productVariantSynchronizator.doImport();
            AppCache.flushCache(coreDAO.getAll(ProductsGroup.class));
            LOG.info("===============================================================================================");
            resetAllCaches();
            indexer.refreshIndex();
            return true;
        } catch (final Exception e) {
            LOG.error("Import MySklad data - fail!", e);
            alertService.sendAlert("Import MySklad data - fail!", e);
            return false;
        }
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
    private void importProducts(Map<String, String> productDiscountMap) throws Exception {
        final var discountsMap = coreDAO.getAll(Discount.class).stream()
            .collect(Collectors.toMap(Discount::getExternalId, Function.identity()));
        List<Product> products = new ArrayList<>();
        Map<String, Product> stock = new HashMap<>();
        int offset = 0;
        while (offset < 6000) {
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
            product.setDiscount(getDiscount(product, productDiscountMap, discountsMap));
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
            eProduct.setTradePrice(actualProduct.getTradePrice());
            eProduct.setTradePriceLimit(actualProduct.getTradePriceLimit());
            eProduct.setGroup(actualProduct.getGroup());
            eProduct.setArticle(actualProduct.getArticle());
            eProduct.setBuyPrice(actualProduct.getBuyPrice());
            if (eProduct.getDescription() == null || eProduct.getDescription().isBlank()) {
                eProduct.setDescription(actualProduct.getDescription());
            }
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
    }

    private Discount getDiscount(
            Product product,
            Map<String, String> productDiscountMap,
            Map<String, Discount> discountsMap
    ) {
        final var discountExternalId = productDiscountMap.get(product.getExternalId());
        if (discountExternalId != null && discountsMap.containsKey(discountExternalId)) {
            return discountsMap.get(discountExternalId);
        } else {
            return null;
        }
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
    
    
    private void resetAllCaches() {
        cacheManager.getCacheNames().parallelStream()
            .forEach(name -> Optional.ofNullable(
                cacheManager.getCache(name)).ifPresent(it -> it.clear())
            );
    }
}
