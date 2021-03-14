/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.platform.dao.CoreDAO;

/**
 * Site static pages controller.
 * @author alex
 */
@Controller
public class SiteController {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SiteController.class);
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
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
    /**
     * Products catalog page.
     * @param model data model.
     * @param page page number.
     * @param view catalog view type.
     * @return JSP page.
     */
    @RequestMapping("/catalog")
    public String catalog(Model model,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view) {
        model.addAttribute("title", "Товары для сада и огорода");
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("view", view == null ? "TILES" : view);
        model.addAttribute("catalog", productDAO.getCatalogProductGroups());
        insertSearchResultToPage(model, null, page);
        return "catalog";
    }
    /**
     * Product group page.
     * @param model data model.
     * @param groupId product group ID.
     * @param page page number.
     * @param view catalog view type.
     * @return JSP page.
     */
    @RequestMapping("/catalog/{groupId}")
    public Object productsGroup(Model model,
            @PathVariable("groupId") Long groupId,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view) throws Exception {
        ProductsGroup group = coreDAO.findById(groupId, ProductsGroup.class);
        if (group == null) {
            return new ModelAndView("redirect:/");
        }
        model.addAttribute("title", group.getName());
        model.addAttribute("groupId", groupId);
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("view", view == null ? "TILES" : view);
        model.addAttribute("breadcrumbLabel", group.getName());
        List<ProductsGroup> path = getPath(group);
        path.remove(group);
        model.addAttribute("breadcrumbPath", path);
        model.addAttribute("catalog", productDAO.getCatalogProductGroups());
        insertSearchResultToPage(model, groupId, page);
        return "catalog";
    }
    /**
     * Product detail page.
     * @param model data model.
     * @param id product ID.
     * @param name product name.
     * @return JSP page.
     */
    @RequestMapping("/product/{id}")
    public Object product(Model model,
            @PathVariable("id") Long id,
            @RequestParam(value = "name", required = false) String name) throws Exception {
        Product product = coreDAO.findById(id, Product.class);
        if (product == null) {
            return new ModelAndView("redirect:/");
        }
        model.addAttribute("title", name);
        model.addAttribute("id", id);
        model.addAttribute("product", product);
        model.addAttribute("groupId", product.getGroup() != null ? product.getGroup().getId() : null);
        model.addAttribute("breadcrumbLabel", name);
        model.addAttribute("breadcrumbPath", getPath(product.getGroup()));
        model.addAttribute("catalog", productDAO.getCatalogProductGroups());
        return "product";
    }
    // ================================================ PRIVATE =======================================================
    /**
     * Get breadcrumbs path.
     * @param group leaf group.
     * @return path.
     * @throws Exception error. 
     */
    private List<ProductsGroup> getPath(ProductsGroup group) throws Exception {
        List<ProductsGroup> allGroups = coreDAO.getAll(ProductsGroup.class);
        List<ProductsGroup> path = new ArrayList<>();
        ProductsGroup current = group;
        while (current != null) {
            path.add(current);
            final String parentId = current.getParentId();
            if (parentId != null) {
                current = allGroups.stream().filter(g -> {
                    return parentId.equals(g.getExternalId());
                }).findFirst().get();
            } else {
                current = null;
            }
        }
        Collections.reverse(path);
        return path;
    }
    
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
}
