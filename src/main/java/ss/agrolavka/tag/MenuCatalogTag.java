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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.model.ProductsGroup;

/**
 * Toolbar dropdown menu catalog.
 * @author alex
 */
@Component
public class MenuCatalogTag extends RequestContextAwareTag {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MenuCatalogTag.class);
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    @Override
    public void doFinally() {
        JspWriter out = pageContext.getOut();
        try {
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
            out.print("<ul>");
            for (ProductsGroup root : roots) {
                out.print(drawCatalogItem(root, groupsMap, 0));
            }
            out.print("</ul>");
        } catch (Exception ex) {
            LOG.error("Menu catalog rendering error!", ex);
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
            childsSb.append("<li class=\"dropdown\"><a href=\"#\"><span>").append(group.getName())
                    .append("</span> <i class=\"bi bi-chevron-right\"></i></a><ul>");
            List<ProductsGroup> childs = groupsMap.get(group.getExternalId());
            Collections.sort(childs);
            for (ProductsGroup child : childs) {
                childsSb.append(drawCatalogItem(child, groupsMap, nextLevel));
            }
            childsSb.append("</ul></li>");
            sb.append(childsSb);
        } else {
            sb.append(String.format("<li><a href=\"/catalog/%s/%s\">%s</a></li>", group.getId(),
                    group.getName(), group.getName()));
        }
        return sb.toString();
    }
    
}
