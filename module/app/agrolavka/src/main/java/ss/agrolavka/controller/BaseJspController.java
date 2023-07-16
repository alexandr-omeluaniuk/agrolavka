package ss.agrolavka.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import ss.agrolavka.constants.JspValue;
import ss.agrolavka.service.OrderService;
import ss.agrolavka.service.ProductService;
import ss.agrolavka.service.ProductsGroupService;
import ss.agrolavka.service.SiteDataService;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;

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
    protected SiteDataService siteDataService;
    
    protected void insertCommonDataToModel(HttpServletRequest request, Model model) throws Exception {
        // cart
        final Order order = orderService.getCurrentOrder(request);
        Double total = 0d;
        for (OrderPosition pos : order.getPositions()) {
            total += pos.getPrice() * pos.getQuantity();
        }
        String totalStr = String.format("%.2f", total);
        String[] parts = totalStr.split("\\.");
        model.addAttribute("cart", order);
        model.addAttribute("totalInteger", parts[0]);
        model.addAttribute("totalDecimal", parts[1]);
        model.addAttribute(JspValue.PRODUCTS_COUNT, productService.getProductsCount());
        model.addAttribute(JspValue.SHOPS, siteDataService.getAllShops());
    }
}
