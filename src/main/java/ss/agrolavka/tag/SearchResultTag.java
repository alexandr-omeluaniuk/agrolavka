/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.tag;

import javax.servlet.jsp.JspWriter;
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.model.Product;

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
    /** Product group ID. */
    private String groupId;
    @Override
    public void doFinally() {
        JspWriter out = pageContext.getOut();
        try {
            out.print("<div class=\"container\">");
            for (int i = 0; i < ROWS; i++) {
                out.print("<div class=\"row\">");
                for (int j = 0; j < COLUMNS; j++) {
                    out.print("<div class=\"col-sm\">");
                    out.print("TODO: Product");
                    out.print("</div>");
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
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    // ============================================== PRIVATE =========================================================
    private String renderProductCard(Product product) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"container mt-5 mb-5 d-flex justify-content-center align-items-center\">");
            sb.append("<div class=\"card\">");
                sb.append("<div class=\"inner-card\"> <img src=\"https://i.imgur.com/4qXhMAM.jpg\" class=\"img-fluid rounded\">");
                    sb.append("<div class=\"d-flex justify-content-between align-items-center mt-3 px-2\">");
                        sb.append("<h4>Worksheet chair </h4> <span class=\"heart\"><i class=\"fa fa-heart\"></i></span>");
                    sb.append("</div>");
                    sb.append("<div class=\"mt-2 px-2\"> <small>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium</small> </div>");
                    sb.append("<div class=\"px-2\">");
                        sb.append("<h3>$500</h3>");
                    sb.append("</div>");
                    sb.append("<div class=\"px-2 mt-3\"> <button class=\"btn btn-primary px-3\">Buy Now</button> <button class=\"btn btn-outline-primary px-3\">Add to cart</button> </div>");
                sb.append("</div>");
            sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }
}
