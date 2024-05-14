package ss.agrolavka.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import ss.agrolavka.constants.JspValue;
import ss.agrolavka.service.*;
import ss.martin.security.configuration.external.DomainConfiguration;

/**
 * Base JSP controller.
 * @author alex
 */
abstract class BaseJspController {
    
    @Autowired
    protected ProductService productService;
    
    @Autowired
    protected ProductsGroupService productsGroupService;
    
    @Autowired
    protected OrderService orderService;

    @Autowired
    protected SessionService sessionService;
    
    @Autowired
    protected SiteDataService siteDataService;
    
    @Autowired
    protected DomainConfiguration domainConfiguration;
    
    protected void setCommonAttributes(final HttpServletRequest request, final Model model) {
        // cart
        final var order = sessionService.getCurrentOrder(request);
        var total = 0d;
        for (final var pos : order.getPositions()) {
            total += pos.getPrice() * pos.getQuantity();
        }
        final var parts = String.format("%.2f", total).split("\\.");
        model.addAttribute(JspValue.CART, order);
        model.addAttribute(JspValue.TOTAL_INTEGER, parts[0]);
        model.addAttribute(JspValue.TOTAL_DECIMAL, parts[1]);
        model.addAttribute(JspValue.SHOPS, siteDataService.getAllShops());
        model.addAttribute(JspValue.DOMAIN, domainConfiguration.host());
        model.addAttribute(JspValue.PURCHASE_HISTORY, orderService.getPurchaseHistory(request));
    }
}
