package ss.agrolavka.test.controller;

import org.junit.jupiter.api.Test;
import ss.agrolavka.constants.JspValue;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.test.common.AgrolavkaDataFactory;
import ss.martin.security.test.DataFactory;

public class CatalogControllerTest extends BasePageControllerTest {
    
    @Test
    public void testCatalogRoot_NoGroups() throws Exception {
        call(SiteUrls.PAGE_CATALOG_ROOT, 
            JspValue.CANONICAL, 
            JspValue.PAGE,
            JspValue.VIEW,
            JspValue.SORT,
            JspValue.AVAILABLE,
            JspValue.TITLE,
            JspValue.META_DESCRIPTION,
            JspValue.CATEGORIES,
            JspValue.PURCHASE_HISTORY_PRODUCTS
        );
    }
    
    @Test
    public void testCatalogRoot_OneGroup() throws Exception {
        DataFactory.silentAuthentication(coreDao);
        final var rootGroup = AgrolavkaDataFactory.generateProductGroup("my-test-root");
        coreDao.create(rootGroup);
        
        call(SiteUrls.PAGE_CATALOG_ROOT, 
            JspValue.CANONICAL, 
            JspValue.PAGE,
            JspValue.VIEW,
            JspValue.SORT,
            JspValue.AVAILABLE,
            JspValue.TITLE,
            JspValue.META_DESCRIPTION,
            JspValue.CATEGORIES,
            JspValue.PURCHASE_HISTORY_PRODUCTS
        );
    }
    
    @Test
    public void testCatalog_NoGroups() throws Exception {
        DataFactory.silentAuthentication(coreDao);
        final var rootGroup = AgrolavkaDataFactory.generateProductGroup("Ups");
        coreDao.create(rootGroup);
        
        call(SiteUrls.PAGE_CATALOG_ROOT + "/ups", 
            JspValue.CANONICAL, 
            JspValue.PAGE,
            JspValue.VIEW,
            JspValue.SORT,
            JspValue.AVAILABLE,
            JspValue.TITLE,
            JspValue.PRODUCT_GROUP,
            JspValue.BREADCRUMB_LABEL,
            JspValue.BREADCRUMB_PATH,
            JspValue.META_DESCRIPTION,
            JspValue.CATEGORIES,
            JspValue.PRODUCTS_SEARCH_RESULT,
            JspValue.PRODUCTS_SEARCH_RESULT_PAGES,
            JspValue.PURCHASE_HISTORY_PRODUCTS
        );
    }
    
    @Test
    public void testProduct() throws Exception {
        DataFactory.silentAuthentication(coreDao);
        final var rootGroup = AgrolavkaDataFactory.generateProductGroup("My test group 2");
        final var group = coreDao.create(rootGroup);
        final var product = AgrolavkaDataFactory.generateProduct(group, "The best product", 100d, 1d);
        coreDao.create(product);
        
        call(SiteUrls.PAGE_CATALOG_ROOT + "/my-test-root-2/the-best-product", 
            JspValue.CANONICAL, 
            JspValue.PAGE,
            JspValue.VIEW,
            JspValue.SORT,
            JspValue.AVAILABLE,
            JspValue.TITLE,
            JspValue.PRODUCT,
            JspValue.BREADCRUMB_LABEL,
            JspValue.BREADCRUMB_PATH,
            JspValue.META_DESCRIPTION,
            JspValue.STRUCTURED_IMAGE,
            JspValue.STRUCTURED_DATA_NAME,
            JspValue.STRUCTURED_DATA_DESCRIPTION,
            JspValue.PRODUCT_PRICE,
            JspValue.PRODUCT_URL,
            JspValue.IN_CART,
            JspValue.VOLUMES,
            JspValue.PRICE_VALID_UNTIL,
            JspValue.FULL_PRODUCT_DESCRIPTION,
            JspValue.VARIANTS,
            JspValue.IN_CART_VARIANTS,
            JspValue.PRODUCT_DISCOUNT
        );
    }
}
