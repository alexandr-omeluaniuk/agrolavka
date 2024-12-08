package ss.agrolavka.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ss.agrolavka.constants.JspPage;
import ss.agrolavka.constants.JspValue;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.service.ProductAttributesService;

import java.util.Collections;

/**
 * Home page controller.
 * @author alex
 */
@Controller
class HomePageController extends BaseJspController {

    @Autowired
    private ProductAttributesService productAttributesService;
    
    @RequestMapping(SiteUrls.PAGE_HOME)
    public String home(Model model, HttpServletRequest httpRequest) {
        setCommonAttributes(httpRequest, model);
        model.addAttribute(JspValue.TITLE, "Все для сада и огорода");
        model.addAttribute(JspValue.TOP_CATEGORIES, productsGroupService.getTopCategories());
        model.addAttribute(
            JspValue.NEW_PRODUCTS,
            productAttributesService.setAttributeLinks(productService.getNewProducts())
        );
        final var slides = siteDataService.getAllSlides();
        Collections.sort(slides);
        model.addAttribute(JspValue.SLIDES, slides);
        final var withDiscount = productAttributesService.setAttributeLinks(productService.getProductsWithDiscount());
        final var withDiscountFirst12 = withDiscount.size() > 12 ? withDiscount.subList(0, 12) : withDiscount;
        model.addAttribute(JspValue.PRODUCTS_WITH_DISCOUNT, withDiscountFirst12);
        model.addAttribute(JspValue.PRODUCTS_COUNT, productService.getProductsCount());
        return JspPage.HOME;
    }
}
