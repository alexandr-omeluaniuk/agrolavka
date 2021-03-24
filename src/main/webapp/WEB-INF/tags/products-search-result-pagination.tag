<%-- 
    Document   : products-search-result-pagination
    Created on : Mar 14, 2021, 3:11:16 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" import="java.util.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="pages" required="true" type="Integer"%>
<%@attribute name="page" required="true" type="Integer"%>
<%@attribute name="view" required="true" type="String"%>
<%@attribute name="url" required="true" type="String"%>

<%-- any content can be specified here e.g.: --%>
<%!
    String outputPagination() {
        StringBuilder pageLinks = new StringBuilder();
        final int shift = 3;
        int cursor = page - shift;
        while (cursor < page) {
            if (cursor > 0) {
                pageLinks.append(renderPage(cursor, page));
            }
            cursor++;
        }
        cursor = Integer.valueOf(page + "");
        while (cursor < page + shift && cursor <= pages) {
            pageLinks.append(renderPage(cursor, page));
            cursor++;
        }
        if (cursor < pages) {
            pageLinks.append("<li class=\"page-item disabled\"><a class=\"page-link\" href=\"#\">...</a></li>");
            pageLinks.append(renderPage(pages, page));
        }
        return pageLinks.toString();
    }
    String renderPage(Integer page, Integer currentPage) {
        StringBuilder sb = new StringBuilder();
        sb.append("<li class=\"page-item ").append(currentPage.equals(page) ? "active" : "")
                .append("\"><a class=\"page-link\" href=\"").append(createLink(page, null))
                .append("\">").append(page).append("</a></li>");
        return sb.toString();
    }
    String createLink(Integer pageParam, String viewParam) {
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
            sb.append(url).append("?");
            params.forEach(p -> {
                sb.append(p).append("&");
            });
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
%>
<div class="row products-toolbar">
    <div class="col-sm-12 col-md-6 d-flex justify-content-start">
        <c:if test="${pages > 1}">
            <nav aria-label="Page navigation" class="d-flex justify-content-start">
                <ul class="pagination" style="margin-bottom: 0;">
                    <li class="page-item ${page == 1 ? " disabled" : ""}">
                        <a class="page-link" href="<%= createLink(1, null) %>" aria-label="Первая страница">
                            <i class="fas fa-angle-double-left"></i>
                        </a>
                    </li>
                    <li class="page-item ${page == 1 ? " disabled" : ""}">
                        <a class="page-link" href="<%= createLink(page - 1, null) %>" aria-label="Назад">
                            <i class="fas fa-angle-left"></i>
                        </a>
                    </li>
                    <%= outputPagination() %>
                    <li class="page-item ${page == pages ? " disabled" : ""}">
                        <a class="page-link" href="<%= createLink(page + 1, null) %>" aria-label="Вперед">
                            <i class="fas fa-angle-right"></i>
                        </a>
                    </li>
                    <li class="page-item ${page == pages ? " disabled" : ""}">
                        <a class="page-link" href="<%= createLink(pages, null) %>" aria-label="Последняя страница">
                            <i class="fas fa-angle-double-right"></i>
                        </a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
    <div class="col-sm-12 col-md-6 d-flex justify-content-end">
        <div class="btn-group">
            <a href="<%= createLink(null, "TILES") %>" class="btn btn-outline-info ${"TILES".equals(view) ? "active" : ""}">
                <i class="fas fa-th" ${"TILES".equals(view) ? " style=\"color: white;\"" : ""}></i>
            </a>
            <a href="<%= createLink(null, "LIST") %>" class="btn btn-outline-info ${"LIST".equals(view) ? "active" : ""}">
                <i class="fas fa-list" ${"LIST".equals(view) ? " style=\"color: white;\"" : ""}></i>
            </a>
        </div>
    </div>
</div>
