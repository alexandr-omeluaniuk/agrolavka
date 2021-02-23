/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.model.Product;

/**
 * Site static pages controller.
 * @author alex
 */
@Controller
public class SiteController {
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /**
     * Home page.
     * @param model data model.
     * @param page page number.
     * @param view view type.
     * @return JSP page.
     */
    @RequestMapping("/")
    public String home(Model model,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view) {
        model.addAttribute("title", "Главная");
        model.addAttribute("page", page);
        model.addAttribute("view", view);
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
     * @param name product group name.
     * @param page page number.
     * @param view catalog view type.
     * @return JSP page.
     */
    @RequestMapping("/catalog/{groupId}")
    public String productsGroup(Model model,
            @PathVariable("groupId") Long groupId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view) {
        model.addAttribute("title", name);
        model.addAttribute("groupId", groupId);
        model.addAttribute("page", page);
        model.addAttribute("view", view);
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
    public String product(Model model,
            @PathVariable("id") Long id,
            @RequestParam(value = "name", required = false) String name) throws Exception {
        Product product = coreDAO.findById(id, Product.class);
        model.addAttribute("title", name);
        model.addAttribute("id", id);
        model.addAttribute("product", product);
        model.addAttribute("groupId", product.getGroup() != null ? product.getGroup().getId() : null);
        return "product";
    }
}
