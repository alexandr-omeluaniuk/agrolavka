package ss.agrolavka.test.controller;

import org.junit.jupiter.api.Test;
import ss.agrolavka.constants.JspValue;
import ss.agrolavka.constants.SiteUrls;

public class HomePageControllerTest extends BasePageControllerTest {
    
    @Test
    public void testHomePage() throws Exception {
        call(SiteUrls.PAGE_HOME, 
            JspValue.NEW_PRODUCTS, 
            JspValue.PRODUCTS_COUNT,
            JspValue.PRODUCTS_WITH_DISCOUNT,
            JspValue.SLIDES,
            JspValue.TOP_CATEGORIES,
            JspValue.TITLE
        );
    }
}
