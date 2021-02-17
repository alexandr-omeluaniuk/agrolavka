/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.model.ProductsGroup;

/**
 * Draw catalog tree.
 * @author alex
 */
@Component
public class CatalogDrawer {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CatalogDrawer.class);
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Accordion item HTML template. */
    private static final String ACCORDION_ITEM_HTML = 
    "<div class=\"accordion-item\">" +
        "<h2 class=\"accordion-header\" id=\"heading-product-group-%s\">" +
            "<button class=\"accordion-button collapsed\" type=\"button\" data-bs-toggle=\"collapse\"" + 
                    "data-bs-target=\"#collapse-product-group-%s\" aria-expanded=\"true\"" + 
                    "aria-controls=\"collapse-product-group-%s\">" +
                "%s" +
            "</button>" +
        "</h2>" +
        "<div id=\"collapse-product-group-%s\" class=\"accordion-collapse collapse\"" +
                "aria-labelledby=\"heading-product-group-%s\" data-bs-parent=\"#catalog-%s\">" +
            "<div class=\"accordion-body\">" +
                "%s" +                    
            "</div>" +
        "</div>" +
    "</div>"; 
    /**
     * Draw catalog HTML template.
     * @return HTML string.
     */
    public String draw() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"accordion\" id=\"catalog-0\">");
            List<ProductsGroup> allGroups = coreDAO.getAll(ProductsGroup.class);
            Map<String, List<ProductsGroup>> groupsMap = new HashMap<>();
            List<ProductsGroup> roots = new ArrayList<>();
            for (ProductsGroup group : allGroups) {
                if (group.getParentId() != null) {
                    if (!groupsMap.containsKey(group.getParentId())) {
                        groupsMap.put(group.getParentId(), new ArrayList<>());
                    }
                    groupsMap.get(group.getParentId()).add(group);
                } else {
                    roots.add(group);
                }
            }
            for (ProductsGroup root : roots) {
                sb.append(drawCatalogItem(root, groupsMap, 0));
            }
            sb.append("</div>");
            return sb.toString();
        } catch (Exception e) {
            return "<h3 class=\"text-danger\">Произошла непредвиденная ошибка, приносим свои извинения</h3>";
        }
    }
    
    private String drawCatalogItem(ProductsGroup group, Map<String, List<ProductsGroup>> groupsMap, Integer level) {
        Integer nextLevel = level + 1;
        StringBuilder sb = new StringBuilder();
        StringBuilder childsSb = new StringBuilder();
        if (groupsMap.containsKey(group.getExternalId())) {
            childsSb.append("<div class=\"accordion\" id=\"catalog-" + nextLevel + "\">");
            for (ProductsGroup child : groupsMap.get(group.getExternalId())) {
                childsSb.append(drawCatalogItem(child, groupsMap, nextLevel));
            }
            childsSb.append("</div>");
        }
        sb.append(String.format(ACCORDION_ITEM_HTML, group.getId(), group.getId(), group.getId(), group.getName(),
                group.getId(), group.getId(), level, childsSb.toString()));
        return sb.toString();
    }
}
