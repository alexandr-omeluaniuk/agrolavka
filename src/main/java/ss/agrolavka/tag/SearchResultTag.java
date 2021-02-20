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
    private static final int ROWS = 4;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    /** Product group ID. */
    private Long groupId;
    @Override
    public void doFinally() {
        JspWriter out = pageContext.getOut();
        ProductsSearchRequest searchRequest = new ProductsSearchRequest();
        searchRequest.setGroupId(groupId);
        searchRequest.setPage(1);
        searchRequest.setPageSize(COLUMNS * ROWS);
        try {
            List<Product> pageProducts = productDAO.search(searchRequest);
            out.print("<div class=\"container\">");
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
    // ============================================== PRIVATE =========================================================
    private String renderProductCard(Product product) {
        if (product == null) {
            return "<div class=\"empty-product-card\"></div>";
        }
        String imageLink = "/api/product-image/" + product.getId();
        StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"card shadow-sm mb-5 bg-body rounded product-card\">");
                sb.append("<div class=\"card-img-top product-image\" style=\"background-image: url('" + imageLink + "')\"></div>");
                sb.append("<div class=\"card-body\">");
                    sb.append("<h5 class=\"card-title\">").append(product.getName()).append("</h5>");
                sb.append("</div>");
            sb.append("</div>");
        return sb.toString();
    }
}
