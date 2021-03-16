/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.util.UrlProducer;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.DataModel;

/**
 * Site static pages controller.
 * @author alex
 */
@Controller
public class SiteController {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SiteController.class);
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    /**
     * Home page.
     * @param model data model.
     * @return JSP page.
     * @throws Exception error.
     */
    @RequestMapping("/")
    public String home(Model model) throws Exception {
        model.addAttribute("title", "Главная");
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(Integer.MAX_VALUE);
        model.addAttribute("productsCount", productDAO.count(request));
        return "home";
    }
//    /**
//     * Products catalog page.
//     * @param model data model.
//     * @param page page number.
//     * @param view catalog view type.
//     * @return JSP page.
//     */
//    @RequestMapping("/catalog")
//    public String catalog(Model model,
//            @RequestParam(name = "page", required = false) Integer page,
//            @RequestParam(name = "view", required = false) String view) {
//        model.addAttribute("title", "Товары для сада и огорода");
//        model.addAttribute("page", page == null ? 1 : page);
//        model.addAttribute("view", view == null ? "TILES" : view);
//        model.addAttribute("groups", UrlProducer.getProductsGroups());
//        insertSearchResultToPage(model, null, page);
//        return "catalog";
//    }
    /**
     * Product group page.
     * @param model data model.
     * @param request HTTP request.
     * @param page page number.
     * @param view catalog view type.
     * @return JSP page.
     * @throws Exception error.
     */
    @RequestMapping("/catalog/**")
    public Object productsGroup(Model model, HttpServletRequest request,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view) throws Exception {
        String url = request.getRequestURI();
        String last = url.substring(url.lastIndexOf("/") + 1);
        DataModel entity = resolveUrlToProductGroup(url);
        if (entity == null) {
            entity = productDAO.getProductByUrl(url);
        }
        if (entity == null) {
            return new ModelAndView("redirect:/");
        }
        if (entity instanceof ProductsGroup) {
            ProductsGroup group = (ProductsGroup) entity;
            model.addAttribute("title", group.getName());
            model.addAttribute("group", group);
            model.addAttribute("page", page == null ? 1 : page);
            model.addAttribute("view", view == null ? "TILES" : view);
            model.addAttribute("breadcrumbLabel", group.getName());
            List<ProductsGroup> path = UrlProducer.getBreadcrumbPath(group);
            path.remove(group);
            model.addAttribute("breadcrumbPath", path);
            model.addAttribute("groups", UrlProducer.getProductsGroups());
            insertSearchResultToPage(model, group.getId(), page);
            return "catalog";
        } else if (entity instanceof Product) {
            Product product = (Product) entity;
            model.addAttribute("title", product.getName());
            model.addAttribute("id", product.getId());
            model.addAttribute("product", product);
            model.addAttribute("groupId", product.getGroup() != null ? product.getGroup().getId() : null);
            model.addAttribute("breadcrumbLabel", product.getName());
            model.addAttribute("breadcrumbPath", UrlProducer.getBreadcrumbPath(product.getGroup()));
            model.addAttribute("groups", UrlProducer.getProductsGroups());
            return "product";
        } else {
            return new ModelAndView("redirect:/");
        }
    }
    // ================================================ PRIVATE =======================================================
    
    private void insertSearchResultToPage(Model model, Long groupId, Integer page) {
        try {
            ProductsSearchRequest searchRequest = new ProductsSearchRequest();
            searchRequest.setGroupId(groupId);
            searchRequest.setPage(page == null ? 1 : page);
            int pageSize = SiteConstants.SEARCH_RESULT_TILES_COLUMNS * SiteConstants.SEARCH_RESULT_TILES_ROWS;
            searchRequest.setPageSize(pageSize);
            model.addAttribute("searchResult", productDAO.search(searchRequest));
            Long count = productDAO.count(searchRequest);
            model.addAttribute(
                    "searchResultPages", Double.valueOf(Math.ceil((double) count / pageSize)).intValue());
        } catch (Exception e) {
            LOG.error("Search products fail!", e);
            model.addAttribute("searchResult", new ArrayList<Product>());
            model.addAttribute("searchResultPages", 0);
        }
    }
    
    /**
     * Resolve URL to product group.
     * @param url url.
     * @return product group.
     */
    public static synchronized ProductsGroup resolveUrlToProductGroup(String url) {
        String last = url.substring(url.lastIndexOf("/") + 1);
        for (ProductsGroup group : UrlProducer.getProductsGroups()) {
            if (last.equals(group.getUrl())) {
                return group;
            }
        }
        return null;
    }
}
