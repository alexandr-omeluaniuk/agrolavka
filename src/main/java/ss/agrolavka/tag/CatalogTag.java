/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspWriter;
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.model.ProductsGroup;

/**
 * Catalog tag.
 * @author alex
 */
public class CatalogTag extends RequestContextAwareTag {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CatalogTag.class);
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
        "<div id=\"collapse-product-group-%s\" class=\"accordion-collapse collapse nested-catalog\"" +
                "aria-labelledby=\"heading-product-group-%s\" data-bs-parent=\"#catalog-%s\">" +
            "<div class=\"accordion-body catalog-body\">" +
                "%s" +                    
            "</div>" +
        "</div>" +
    "</div>";
    /** Accordion leaf item HTML template. */
    private static final String ACCORDION_ITEM_HTML_LEAF = 
    "<div class=\"accordion-item\">" +
        "<h2 class=\"accordion-header\" id=\"heading-product-group-%s\">" +
            "<a class=\"accordion-button collapsed accordion-button-leaf\" type=\"button\" " +
                    "data-bs-toggle=\"collapse\"" + 
                    "data-bs-target=\"#collapse-product-group-%s\" aria-expanded=\"true\"" + 
                    "aria-controls=\"collapse-product-group-%s\" href=\"/catalog/%s/%s\">" +
                "%s" +
            "</a>" +
        "</h2>" +
    "</div>";
    @Override
    public void doFinally() {
        JspWriter out = pageContext.getOut();
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
            Collections.sort(roots);
            for (ProductsGroup root : roots) {
                sb.append(drawCatalogItem(root, groupsMap, 0));
            }
            sb.append("</div>");
            out.print(sb.toString());
        } catch (Exception ex) {
            LOG.error("Catalog rendering error!", ex);
        }
    }
    @Override
    protected int doStartTagInternal() throws Exception {
        if (coreDAO == null) {
            WebApplicationContext context = getRequestContext().getWebApplicationContext();
            AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
            autowireCapableBeanFactory.autowireBean(this);
        }
        return SKIP_BODY;
    }
    /**
     * Draw catalog item.
     * @param group products group.
     * @param groupsMap groups map.
     * @param level item level.
     * @return HTM template.
     */
    private String drawCatalogItem(ProductsGroup group, Map<String, List<ProductsGroup>> groupsMap, Integer level) {
        Integer nextLevel = level + 1;
        StringBuilder sb = new StringBuilder();
        StringBuilder childsSb = new StringBuilder();
        if (groupsMap.containsKey(group.getExternalId())) {
            childsSb.append("<div class=\"accordion\" id=\"catalog-" + nextLevel
                    + "\" style=\"padding-left: 32px\">");
            List<ProductsGroup> childs = groupsMap.get(group.getExternalId());
            Collections.sort(childs);
            for (ProductsGroup child : childs) {
                childsSb.append(drawCatalogItem(child, groupsMap, nextLevel));
            }
            childsSb.append("</div>");
            sb.append(String.format(ACCORDION_ITEM_HTML, group.getId(), group.getId(), group.getId(), group.getName(),
                group.getId(), group.getId(), level, childsSb.toString()));
        } else {
            sb.append(String.format(ACCORDION_ITEM_HTML_LEAF, group.getId(), group.getId(), group.getId(),
                    group.getId(), group.getName(), group.getName()));
        }
        return sb.toString();
    }
}
