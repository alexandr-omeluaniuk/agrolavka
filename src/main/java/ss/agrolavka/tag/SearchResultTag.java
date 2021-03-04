/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.tag;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.entity.Product;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.martin.platform.dao.CoreDAO;

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
    private static final int ROWS = 5;
    /** List view. */
    private static final String VIEW_LIST = "LIST";
    /** Tiles view. */
    private static final String VIEW_TILES = "TILES";
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
    /** View. TILES or LIST. */
    private String view;
    /** URL. */
    private String url;
    @Override
    public void doFinally() {
        JspWriter out = pageContext.getOut();
        ProductsSearchRequest searchRequest = new ProductsSearchRequest();
        searchRequest.setGroupId(groupId);
        searchRequest.setPage(page == null ? 1 : page);
        searchRequest.setPageSize(COLUMNS * ROWS);
        try {
            String pagination = renderPagination(searchRequest);
            List<Product> pageProducts = productDAO.search(searchRequest);
            out.print("<div class=\"row products-search-result\">");
            out.print("<div class=\"col-sm-12\">");
            out.print(renderToolbar(searchRequest, pagination));
            if (pageProducts.isEmpty()) {
                out.print("<p class=\"text-center\">По вашему запросу ничего не найдено</p>");
            } else {
                if (VIEW_LIST.equals(view)) {
                    out.print(list(pageProducts));
                } else {
                    out.print(tiles(pageProducts));
                }
            }
            out.print("<div class=\"row\">"
                        + "<div class=\"col-12 d-flex justify-content-center\">"
                            + pagination
                        + "</div>"
                    + "</div>");
            out.print("</div>");
            out.print("</div>");
        } catch (Exception ex) {
            LOG.error("Search result rendering error!", ex);
        }
    }
    @Override
    protected int doStartTagInternal() throws Exception {
        if (coreDAO == null || productDAO == null) {
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
    /**
     * @param view the view to set
     */
    public void setView(String view) {
        this.view = view;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    // ============================================== PRIVATE =========================================================
    /**
     * Render tiles view.
     * @param pageProducts page products.
     * @return HTML template.
     */
    private String tiles(List<Product> pageProducts) throws Exception {
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (int i = 0; i < ROWS; i++) {
            sb.append("<div class=\"row\">");
            for (int j = 0; j < COLUMNS; j++) {
                sb.append("<div class=\"col-sm\">");
                sb.append(renderProductCard(pageProducts.size() > counter ? pageProducts.get(counter) : null));
                sb.append("</div>");
                counter++;
            }
            sb.append("</div>");
        }
        return sb.toString();
    }
    /**
     * Render list view.
     * @param pageProducts page products.
     * @return HTML template.
     */
    private String list(List<Product> pageProducts) throws Exception {
        StringBuilder content = new StringBuilder();
        for (Product product: pageProducts) {
            String imageLink = "/api/agrolavka/public/product-image/" + product.getId();
            content.append("<tr>").append("<a href=\"/product/" + product.getId()
                    + "?name=" + URLEncoder.encode(product.getName(), "UTF-8") + "\">");
                content.append("<th scope=\"row\" style=\"padding: 0;\"><img src=\"").append(imageLink)
                        .append("\" class=\"product-avatar img-thumbnail\" alt=\"")
                        .append(product.getName()).append("\"></th>");
                content.append("<td>").append(product.getName()).append("</td>");
                content.append("<td class=\"text-end\">")
                        .append(String.format("%.2f", product.getPrice())).append(" BYN</td>");
            content.append("</a></tr>");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"table-responsive\">");
        sb.append("<table class=\"table table-hover table-bordered rounded product-table\">");
            sb.append("<thead class=\"table-light\">");
                sb.append("<tr>");
                    sb.append("<th scope=\"col\" width=\"80px\"></th>");
                    sb.append("<th scope=\"col\">Наименование</th>");
                    sb.append("<th class=\"text-end\" scope=\"col\" width=\"130px\">Цена</th>");
                sb.append("</tr>");
            sb.append("</thead>");
            sb.append("<tbody>");
                sb.append(content.toString());
            sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div>");
        return sb.toString();
    }
    /**
     * Render product card.
     * @param product product.
     * @return product card HTML template.
     */
    private String renderProductCard(Product product) throws Exception {
        if (product == null) {
            return "<div class=\"empty-product-card\"></div>";
        }
        String imageLink = "/api/agrolavka/public/product-image/" + product.getId();
        StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"card shadow-sm mb-5 bg-body rounded product-card\">");
                sb.append("<div class=\"card-img-top product-image\" style=\"background-image: url('")
                        .append(imageLink).append("')\"></div>");
                sb.append("<div class=\"card-body\">");
                    sb.append("<h6 class=\"card-title\" style=\"min-height: 60px\">")
                            .append(product.getName()).append("</h6>");
                    sb.append("<div class=\"d-flex justify-content-between align-items-center\">"
                            + "<span class=\"card-subtitle text-muted fs-6\">Цена</span>"
                            + "<span class=\"text-dark fw-bold product-price\">"
                            + String.format("%.2f", product.getPrice())
                            + " BYN</span>"
                            + "</div>");
                sb.append("</div>");
            sb.append("</div>");
        return "<a href=\"/product/" + product.getId() + "?name=" + URLEncoder.encode(product.getName(), "UTF-8")
                + "\">" + sb.toString() + "</a>";
    }
    /**
     * Render search result toolbar.
     * @param searchRequest search request.
     * @return toolbar HTML template.
     */
    private String renderToolbar(ProductsSearchRequest searchRequest, String pagination) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"row products-toolbar\">");
            sb.append("<div class=\"col-6 d-flex justify-content-start\">");
                sb.append(pagination);
            sb.append("</div>");
            sb.append("<div class=\"col-6 d-flex justify-content-end\">");
                sb.append(renderViewSwitcher());
            sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }
    /**
     * Render pagination component.
     * @param searchRequest search request.
     * @return pagination HTML template.
     */
    private String renderPagination(ProductsSearchRequest searchRequest) throws Exception {
        Long count = productDAO.count(searchRequest);
        int pagesCount = Double.valueOf(Math.ceil((double) count / (ROWS * COLUMNS))).intValue();
        if (pagesCount == 1) {
            return "";
        }
        Integer aPage = page != null ? page : 1;
        StringBuilder pageLinks = new StringBuilder();
        pageLinks.append("<li class=\"page-item ").append(searchRequest.getPage().equals(aPage) ? "active" : "")
                .append("\"><a class=\"page-link\" href=\"").append(createLink(aPage, null))
                .append("\">").append(aPage).append("</a></li>");
        if (aPage != pagesCount) {
            pageLinks.append("<li class=\"page-item disabled\"><a class=\"page-link\" href=\"#\">...</a></li>");
            pageLinks.append("<li class=\"page-item\"><a class=\"page-link\" href=\"")
                    .append(createLink(pagesCount, null))
                    .append("\">").append(pagesCount).append("</a></li>");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<nav aria-label=\"Page navigation\" class=\"d-flex justify-content-start\">" +
            "<ul class=\"pagination\" style=\"margin-bottom: 0;\">" +
                "<li class=\"page-item" + (aPage == 1 ? " disabled" : "") + "\">" +
                    "<a class=\"page-link \" href=\"" + createLink(1, null) + "\" aria-label=\"Первая страница\">" +
                        "<i class=\"fas fa-angle-double-left\"></i>" +
                    "</a>" +
                "</li>" +
                "<li class=\"page-item" + (aPage == 1 ? " disabled" : "") + "\">" +
                    "<a class=\"page-link \" href=\"" + createLink(aPage - 1, null) + "\" aria-label=\"Назад\">" +
                        "<i class=\"fas fa-angle-left\"></i>" +
                    "</a>" +
                "</li>" +
                pageLinks.toString() +
                "<li class=\"page-item" + (aPage == pagesCount ? " disabled" : "") + "\">" +
                    "<a class=\"page-link \" href=\"" + createLink(aPage + 1, null) + "\" aria-label=\"Вперед\">" +
                        "<i class=\"fas fa-angle-right\"></i>" +
                    "</a>" +
                "</li>" +
                "<li class=\"page-item" + (aPage == pagesCount ? " disabled" : "") + "\">" +
                    "<a class=\"page-link\" href=\"" + createLink(pagesCount, null) +
                            "\" aria-label=\"Последняя страница\">" +
                        "<i class=\"fas fa-angle-double-right\"></i>" +
                    "</a>" +
                "</li>" +
            "</ul>" +
        "</nav>");
        return sb.toString();
    }
    /**
     * Render view switcher.
     * @return view switcher HTML template.
     */
    private String renderViewSwitcher() {
        String aView = view == null || view.isEmpty() ? VIEW_TILES : view;
        return "<div class=\"btn-group\">" +
            "<a href=\"" + createLink(null, VIEW_TILES) + "\" class=\"btn btn-outline-info "
                    + (VIEW_TILES.equals(aView) ? "active" : "") + "\">" +
                "<i class=\"fas fa-th\"" + (VIEW_TILES.equals(aView) ? " style=\"color: white;\"" : "") + "></i>" +
            "</a>" +
            "<a href=\"" + createLink(null, VIEW_LIST) + "\" class=\"btn btn-outline-info "
                    + (VIEW_LIST.equals(aView) ? "active" : "") + "\">" +
                "<i class=\"fas fa-list\"" + (VIEW_LIST.equals(aView) ? " style=\"color: white;\"" : "") + "></i>" +
            "</a>" +
        "</div>";
    }
    /**
     * Create link for page.
     * @param pageParam page number parameter, optional.
     * @param viewParam view type parameter, optional.
     * @return link parameters string.
     */
    private String createLink(Integer pageParam, String viewParam) {
        List<String> params = new ArrayList();
        if (pageParam != null) {
            params.add("page=" + pageParam);
        } else if (page != null) {
            params.add("page=" + page);
        }
        if (viewParam != null) {
            params.add("view=" + viewParam);
        } else if (view != null && !view.isEmpty()) {
            params.add("view=" + view);
        }
        StringBuilder sb = new StringBuilder();
        if (!params.isEmpty()) {
            if (url != null && !url.isEmpty()) {
                sb.append(url);
            }
            sb.append("?");
            params.forEach(p -> {
                sb.append(p).append("&");
            });
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
