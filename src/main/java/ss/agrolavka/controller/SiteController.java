/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
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
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
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
     * @param httpRequest HTTP request.
     * @return JSP page.
     * @throws Exception error.
     */
    @RequestMapping("/")
    public String home(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCartDataToModel(httpRequest, model);
        model.addAttribute("title", "Все для сада и огорода");
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(Integer.MAX_VALUE);
        model.addAttribute("productsCount", productDAO.count(request));
        ProductsSearchRequest searchRequest = new ProductsSearchRequest();
        searchRequest.setPage(1);
        searchRequest.setPageSize(4);
        searchRequest.setOrder("desc");
        searchRequest.setOrderBy("created_date");
        model.addAttribute("newProducts", productDAO.search(searchRequest));
        return "home";
    }
    /**
     * Cart page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     */
    @RequestMapping("/cart")
    public String cart(Model model, HttpServletRequest httpRequest) {
        insertCartDataToModel(httpRequest, model);
        return "cart";
    }
    /**
     * Page not found.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page name.
     */
    @RequestMapping("/error/page-not-found")
    public String error404(Model model, HttpServletRequest httpRequest) {
        insertCartDataToModel(httpRequest, model);
        return "error/404";
    }
    /**
     * Product group page.
     * @param model data model.
     * @param request HTTP request.
     * @param page page number.
     * @param view catalog view type.
     * @param sort sort by.
     * @return JSP page.
     * @throws Exception error.
     */
    @RequestMapping("/catalog/**")
    public Object productsGroup(Model model, HttpServletRequest request,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view,
            @RequestParam(name = "sort", required = false) String sort) throws Exception {
        insertCartDataToModel(request, model);
        String url = request.getRequestURI();
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("view", view == null ? "TILES" : view);
        model.addAttribute("sort", sort == null ? "alphabet" : sort);
        model.addAttribute("groups", UrlProducer.getProductsGroups());
        if ("/catalog".equals(url)) {
            model.addAttribute("canonical", url  + (page != null ? "?page=" + page : ""));
            model.addAttribute("title", "Каталог");
            model.addAttribute("metaDescription", "Каталог товаров для сада и огорода");
            insertSearchResultToPage(model, null, page, sort == null ? "alphabet" : sort);
            return "catalog";
        }
        DataModel entity = resolveUrlToProductGroup(url);
        if (entity == null) {
            return new ModelAndView("redirect:/error/page-not-found");
        }
        if (entity instanceof ProductsGroup) {
            model.addAttribute("canonical", url + (page != null ? "?page=" + page : ""));
            ProductsGroup group = (ProductsGroup) entity;
            model.addAttribute("title", group.getSeoTitle() != null ? group.getSeoTitle() : group.getName());
            model.addAttribute("group", group);
            model.addAttribute("breadcrumbLabel", group.getName());
            List<ProductsGroup> path = UrlProducer.getBreadcrumbPath(group);
            path.remove(group);
            model.addAttribute("breadcrumbPath", path);
            insertSearchResultToPage(model, group.getId(), page, sort);
            String description = group.getDescription();
            String meta = "Купить " + group.getName();
            if (group.getSeoDescription() != null && !group.getSeoDescription().isBlank()) {
                meta = group.getSeoDescription();
            } else {
                if (description != null) {
                    meta = (description.length() > 255 ? description.substring(0, 255) : description);
                }
            }
            model.addAttribute("metaDescription", meta);
            return "catalog";
        } else if (entity instanceof Product) {
            model.addAttribute("canonical", url);
            Product product = (Product) entity;
            model.addAttribute("title", product.getSeoTitle() != null
                    ? product.getSeoTitle() : "Купить " + product.getGroup().getName() + " " + product.getName()
                    + ". Способ применения, инструкция, описание " + product.getName());
            model.addAttribute("id", product.getId());
            model.addAttribute("product", product);
            model.addAttribute("groupId", product.getGroup() != null ? product.getGroup().getId() : null);
            model.addAttribute("breadcrumbLabel", product.getName());
            model.addAttribute("breadcrumbPath", UrlProducer.getBreadcrumbPath(product.getGroup()));
            model.addAttribute("productPrice", String.format("%.2f", product.getPrice()));
            model.addAttribute("productURL", "https://agrolavka.by" + request.getRequestURI());
            String description = product.getDescription();
            String meta = "Купить " + product.getName();
            if (product.getSeoDescription() != null && !product.getSeoDescription().isBlank()) {
                meta = product.getSeoDescription();
            } else {
                if (description != null) {
                    meta = (description.length() > 255 ? description.substring(0, 255) : description);
                }
            }
            model.addAttribute("metaDescription", meta);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            model.addAttribute("priceValidUntil", sdf.format(calendar.getTime()));
            return "product";
        } else {
            return new ModelAndView("redirect:/error/page-not-found");
        }
    }
    // ================================================ PRIVATE =======================================================
    /**
     * Insert search result to page.
     * @param model page model.
     * @param groupId product group ID.
     * @param page page number.
     * @param sort sort by.
     */
    private void insertSearchResultToPage(Model model, Long groupId, Integer page, String sort) {
        try {
            ProductsSearchRequest searchRequest = new ProductsSearchRequest();
            searchRequest.setGroupId(groupId);
            searchRequest.setPage(page == null ? 1 : page);
            int pageSize = SiteConstants.SEARCH_RESULT_TILES_COLUMNS * SiteConstants.SEARCH_RESULT_TILES_ROWS;
            searchRequest.setPageSize(pageSize);
            if ("alphabet".equals(sort)) {
                searchRequest.setOrder("asc");
                searchRequest.setOrderBy("name");
            } else if ("cheap".equals(sort)) {
                searchRequest.setOrder("asc");
                searchRequest.setOrderBy("price");
            } else if ("expensive".equals(sort)) {
                searchRequest.setOrder("desc");
                searchRequest.setOrderBy("price");
            }
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
     * Resolve URL to product group or product.
     * @param url url.
     * @return product group or product.
     */
    private DataModel resolveUrlToProductGroup(String url) {
        String last = url.substring(url.lastIndexOf("/") + 1);
        for (ProductsGroup group : UrlProducer.getProductsGroups()) {
            if (last.equals(group.getUrl())) {
                return group;
            }
        }
        return productDAO.getProductByUrl(last);
    }
    /**
     * Insert cart to data model.
     * @param request HTTP request.
     * @param model page model.
     */
    private void insertCartDataToModel(HttpServletRequest request, Model model) {
        Order order = (Order) request.getSession(true).getAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        if (order == null) {
            order = new Order();
            order.setPositions(new HashSet<>());
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        Double total = 0d;
        for (OrderPosition pos : order.getPositions()) {
            total += pos.getPrice() * pos.getQuantity();
        }
        String totalStr = String.format("%.2f", total);
        String[] parts = totalStr.split("\\.");
        model.addAttribute("cart", order);
        model.addAttribute("totalInteger", parts[0]);
        model.addAttribute("totalDecimal", parts[1]);
    }
}
