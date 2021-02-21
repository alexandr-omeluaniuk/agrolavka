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
    /** Product group ID. */
    private Long groupId;
    @Override
    public void doFinally() {
        JspWriter out = pageContext.getOut();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"card\" style=\"width: 100%; border: none;\">");
                sb.append("<div class=\"card-body d-flex justify-content-between\" "
                        + "style=\"border: none; padding-top: 0px; padding-right: 0px; padding-left: 0px;\">");
                    sb.append("<h5 style=\"margin-bottom: 1rem;\" class=\"text-info\">"
                            + "<i class=\"fas fa-seedling\"></i> Каталог товаров</h5>");
                sb.append("</div>");
            sb.append("</div>");
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
    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
            childsSb.append("<div class=\"accordion\" id=\"catalog-")
                    .append(nextLevel).append("\" style=\"padding-left: 32px\">");
            List<ProductsGroup> childs = groupsMap.get(group.getExternalId());
            Collections.sort(childs);
            for (ProductsGroup child : childs) {
                childsSb.append(drawCatalogItem(child, groupsMap, nextLevel));
            }
            childsSb.append("</div>");
            boolean expanded = isExpanded(group, groupsMap);
            sb.append("<div class=\"accordion-item\">" +
                "<h2 class=\"accordion-header\" id=\"heading-product-group-" + group.getId() + "\">" +
                    "<button class=\"accordion-button " + (expanded ? "" : "collapsed") +
                            "\" type=\"button\" data-bs-toggle=\"collapse\"" + 
                            "data-bs-target=\"#collapse-product-group-" + group.getId() + "\" aria-expanded=\"true\"" + 
                            "aria-controls=\"collapse-product-group-" + group.getId() + "\">" +
                        group.getName() +
                    "</button>" +
                "</h2>" +
                "<div id=\"collapse-product-group-" + group.getId() +
                        "\" class=\"accordion-collapse collapse nested-catalog " + (expanded ? "show" : "") + "\"" +
                        "aria-labelledby=\"heading-product-group-" + group.getId() + "\" data-bs-parent=\"#catalog-" + level + "\">" +
                    "<div class=\"accordion-body catalog-body\">" +
                        childsSb.toString() +                    
                    "</div>" +
                "</div>" +
            "</div>");
        } else {
            sb.append("<div class=\"accordion-item\">" +
                "<h2 class=\"accordion-header\" id=\"heading-product-group-" + group.getId() + "\">" +
                    "<a class=\"accordion-button collapsed accordion-button-leaf link-dark " +
                                (groupId != null && groupId.equals(group.getId()) ? "fw-bold" : "") + "\" type=\"button\" " +
                            "data-bs-toggle=\"collapse\"" + 
                            "data-bs-target=\"#collapse-product-group-" + group.getId() + "\" aria-expanded=\"true\"" + 
                            "aria-controls=\"collapse-product-group-" + group.getId() +
                                "\" href=\"/catalog/" + group.getId() + "/" + group.getName() + "\">" +
                        group.getName() +
                    "</a>" +
                "</h2>" +
            "</div>");
        }
        return sb.toString();
    }
    
    private boolean isExpanded(ProductsGroup group, Map<String, List<ProductsGroup>> groupsMap) {
        if (groupId == null || !groupsMap.containsKey(group.getExternalId())) {
            return false;
        } else {
            List<ProductsGroup> childs = groupsMap.get(group.getExternalId());
            boolean isExpanded = false;
            for (ProductsGroup child : childs) {
                if (groupId.equals(child.getId()) || isExpanded(child, groupsMap)) {
                    isExpanded = true;
                    break;
                }
            }
            return isExpanded;
        }
    }
}
