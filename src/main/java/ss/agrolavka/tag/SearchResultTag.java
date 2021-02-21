/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.tag;

import java.util.List;
import javax.servlet.jsp.JspWriter;
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.model.Product;
import ss.agrolavka.wrapper.ProductsSearchRequest;

/**
 * Search result tag.
 * @author alex
 */
public class SearchResultTag extends RequestContextAwareTag {
/** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SearchResultTag.class);
    /** Columns. */
    private static final int COLUMNS = 3;
    /** Rows. */
    private static final int ROWS = 3;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    /** Product group ID. */
    private Long groupId;
    /** Page. */
    private Integer page;
    @Override
    public void doFinally() {
        JspWriter out = pageContext.getOut();
        ProductsSearchRequest searchRequest = new ProductsSearchRequest();
        searchRequest.setGroupId(groupId);
        searchRequest.setPage(page == null ? 1 : page);
        searchRequest.setPageSize(COLUMNS * ROWS);
        try {
            List<Product> pageProducts = productDAO.search(searchRequest);
            out.print("<div class=\"container\">");
            out.print(renderToolbar(searchRequest));
            int counter = 0;
            for (int i = 0; i < ROWS; i++) {
                out.print("<div class=\"row\">");
                for (int j = 0; j < COLUMNS; j++) {
                    out.print("<div class=\"col-sm\">");
                    out.print(renderProductCard(pageProducts.size() > counter ? pageProducts.get(counter) : null));
                    out.print("</div>");
                    counter++;
                }
                out.print("</div>");
            }
            out.print("</div>");
        } catch (Exception ex) {
            LOG.error("Search result rendering error!", ex);
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
     * @param groupId the groupId to set
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }
    // ============================================== PRIVATE =========================================================
    /**
     * Render product card.
     * @param product product.
     * @return product card HTML template.
     */
    private String renderProductCard(Product product) {
        if (product == null) {
            return "<div class=\"empty-product-card\"></div>";
        }
        String imageLink = "/api/product-image/" + product.getId();
        StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"card shadow-sm mb-5 bg-body rounded product-card\">");
                sb.append("<div class=\"card-img-top product-image\" style=\"background-image: url('")
                        .append(imageLink).append("')\"></div>");
                sb.append("<div class=\"card-body\">");
                    sb.append("<h6 class=\"card-title\">").append(product.getName()).append("</h6>");
                    sb.append("<div class=\"d-flex justify-content-between align-items-center\">"
                            + "<span class=\"card-subtitle text-muted fs-6\">Цена</span>"
                            + "<span class=\"text-dark fw-bold product-price\">"
                            + String.format("%.2f", product.getPrice())
                            + " BYN</span>"
                            + "</div>");
                sb.append("</div>");
            sb.append("</div>");
        return sb.toString();
    }
    
    private String renderToolbar(ProductsSearchRequest searchRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"card\" style=\"width: 100%; border: none;\">");
            sb.append("<div class=\"card-body d-flex justify-content-between\" "
                    + "style=\"border: none; padding-top: 0px; padding-right: 0px; padding-left: 0px;\">");
                sb.append(renderPagination(searchRequest));
                sb.append("<div class=\"btn-group\">" +
                            "<a href=\"?view=tiles\" class=\"btn btn-outline-info active\" aria-current=\"page\">" +
                                "<i class=\"fas fa-th\"></i>" +
                            "</a>" +
                            "<a href=\"?view=list\" class=\"btn btn-outline-info\">" +
                                "<i class=\"fas fa-list\"></i>" +
                            "</a>" +
                        "</div>");
            sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }
    
    private String renderPagination(ProductsSearchRequest searchRequest) {
        Long count = productDAO.count(searchRequest);
        int pagesCount = Double.valueOf(Math.ceil((double) count / (ROWS * COLUMNS))).intValue();
        if (pagesCount == 1) {
            return "";
        }
        StringBuilder pageLinks = new StringBuilder();
        for (int i = 0; i < pagesCount; i++) {
            int p = i + 1;
            pageLinks.append("<li class=\"page-item ").append(searchRequest.getPage().equals(p) ? "active" : "")
                    .append("\"><a class=\"page-link\" href=\"?page=")
                    .append(p).append("\">").append(p).append("</a></li>");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<nav aria-label=\"Page navigation\" class=\"d-flex justify-content-start\">" +
            "<ul class=\"pagination\" style=\"margin-bottom: 0;\">" +
                "<li class=\"page-item\">" +
                    "<a class=\"page-link \" href=\"?page=1\" aria-label=\"Previous\">" +
                        "<i class=\"fas fa-angle-double-left\"></i>" +
                    "</a>" +
                "</li>" +
                pageLinks.toString() +
                "<li class=\"page-item\">" +
                    "<a class=\"page-link\" href=\"?page=" + pagesCount + "\" aria-label=\"Next\">" +
                        "<i class=\"fas fa-angle-double-right\"></i>" +
                    "</a>" +
                "</li>" +
            "</ul>" +
        "</nav>");
        return sb.toString();
    }
}
