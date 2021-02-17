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
import ss.agrolavka.ui.CatalogDrawer;

/**
 *
 * @author alex
 */
@Controller
public class SiteController {
    @Autowired
    private CatalogDrawer catalogDrawer;
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("catalog", catalogDrawer.draw());
        model.addAttribute("title", "Главная");
        return "home";
    }
    @RequestMapping("/about")
    public String about(Model model) {
        return "about";
    }
    @RequestMapping("/catalog/{groupId}/{name}")
    public String productsGroup(Model model, @PathVariable("groupId") String groupId,
            @PathVariable("name") String name) {
        model.addAttribute("catalog", catalogDrawer.draw());
        model.addAttribute("title", name);
        return "home";
    }
}
