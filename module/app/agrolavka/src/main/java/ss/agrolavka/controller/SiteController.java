package ss.agrolavka.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ss.agrolavka.service.SystemSettingsService;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.EuropostLocation;
import ss.martin.core.dao.CoreDao;

import java.util.Collections;
import java.util.List;

import static ss.agrolavka.constants.JspValue.*;

/**
 * Site static pages controller.
 * @author alex
 */
@Controller
public class SiteController extends BaseJspController {

    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;

    @Autowired
    private SystemSettingsService systemSettingsService;
    
    /**
     * Cart page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error.
     */
    @RequestMapping("/cart")
    public String cart(Model model, HttpServletRequest httpRequest) throws Exception {
        setCommonAttributes(httpRequest, model);
        return "cart";
    }
    /**
     * Order.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error.
     */
    @RequestMapping("/order")
    public String order(Model model, HttpServletRequest httpRequest) throws Exception {
        setCommonAttributes(httpRequest, model);
        final List<EuropostLocation> locations = coreDAO.getAll(EuropostLocation.class);
        Collections.sort(locations);
        model.addAttribute("europostLocations", locations);
        return "order";
    }
    /**
     * Delivery page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error. 
     */
    @RequestMapping("/delivery")
    public String delivery(Model model, HttpServletRequest httpRequest) throws Exception {
        setCommonAttributes(httpRequest, model);
        model.addAttribute(SYSTEM_SETTINGS, systemSettingsService.getCurrentSettings());
        return "delivery";
    }
    /**
     * Discount page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error. 
     */
    @RequestMapping("/discount")
    public String discount(Model model, HttpServletRequest httpRequest) throws Exception {
        setCommonAttributes(httpRequest, model);
        return "discount";
    }
    /**
     * Promotions page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error.
     */
    @RequestMapping("/promotions")
    public String promotions(Model model, HttpServletRequest httpRequest) throws Exception {
        setCommonAttributes(httpRequest, model);
        model.addAttribute("products", productService.getProductsWithDiscount());
        return "promotions";
    }
    
    @RequestMapping("/shops")
    public String shops(Model model, HttpServletRequest httpRequest) throws Exception {
        setCommonAttributes(httpRequest, model);
        return "shops";
    }

    @RequestMapping("/shops/{title}")
    public String shop(@PathVariable String title, Model model, HttpServletRequest request) {
        setCommonAttributes(request, model);
        final var shop = siteDataService.getAllShops().stream()
                .filter(s -> title.equals(UrlProducer.transliterate(s.getTitle()))).findFirst().get();
        model.addAttribute(TITLE, shop.getTitle());
        model.addAttribute(META_DESCRIPTION, shop.getDescription());
        model.addAttribute(CANONICAL, request.getRequestURI());
        model.addAttribute(SHOP, shop);
        return "shop";
    }
    /**
     * Feedback page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error.
     */
    @RequestMapping("/feedback")
    public String feedback(Model model, HttpServletRequest httpRequest) throws Exception {
        setCommonAttributes(httpRequest, model);
        return "feedback";
    }
    /**
     * Page not found.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page name.
     */
    @RequestMapping("/error/page-not-found")
    public String error404(Model model, HttpServletRequest httpRequest) {
        setCommonAttributes(httpRequest, model);
        return "error/404";
    }
}
