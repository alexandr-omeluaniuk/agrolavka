/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ss.agrolavka.dao.ProductDAO;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.martin.platform.dao.CoreDAO;

/**
 * Site static pages controller.
 * @author alex
 */
@Controller
public class SiteController {
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    /**
     * Home page.
     * @param model data model.
     * @param page page number.
     * @param view view type.
     * @return JSP page.
     * @throws Exception error.
     */
    @RequestMapping("/")
    public String home(Model model,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view) throws Exception {
        model.addAttribute("title", "Главная");
        model.addAttribute("page", page);
        model.addAttribute("view", view);
        ProductsSearchRequest request = new ProductsSearchRequest();
        request.setPage(1);
        request.setPageSize(Integer.MAX_VALUE);
        model.addAttribute("productsCount", productDAO.count(request));
        return "home";
    }
    /**
     * About page.
     * @param model data model.
     * @return JSP page.
     */
    @RequestMapping("/about")
    public String about(Model model) {
        return "about";
    }
    /**
     * Products catalog page.
     * @param model data model.
     * @param page page number.
     * @param view catalog view type.
     * @return JSP page.
     */
    @RequestMapping("/catalog")
    public String productsGroup(Model model,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view) {
        model.addAttribute("title", "Товары для сада и огорода");
        model.addAttribute("page", page);
        model.addAttribute("view", view);
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
        model.addAttribute("page", page);
        model.addAttribute("view", view);
        model.addAttribute("breadcrumbLabel", group.getName());
        List<ProductsGroup> path = getPath(group);
        path.remove(group);
        model.addAttribute("breadcrumbPath", path);
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
        return "product";
    }
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
}
