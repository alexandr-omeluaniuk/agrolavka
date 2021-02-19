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
}
