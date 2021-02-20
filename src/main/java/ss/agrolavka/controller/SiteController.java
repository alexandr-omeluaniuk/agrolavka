/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author alex
 */
@Controller
public class SiteController {
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная");
        return "home";
    }
    @RequestMapping("/about")
    public String about(Model model) {
        return "about";
    }
    @RequestMapping("/catalog")
    public String productsGroup(Model model) {
        model.addAttribute("title", "Каталог");
        return "catalog";
    }
    @RequestMapping("/catalog/{groupId}/{name}")
    public String productsGroup(Model model, @PathVariable("groupId") Long groupId,
            @PathVariable("name") String name) {
        model.addAttribute("title", name);
        model.addAttribute("groupId", groupId);
        return "catalog";
    }
}
