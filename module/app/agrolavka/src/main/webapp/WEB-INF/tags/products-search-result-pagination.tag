<%-- 
    Document   : products-search-result-pagination
    Created on : Mar 14, 2021, 3:11:16 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag import="ss.agrolavka.util.AppCache"%>
<%@tag import="ss.entity.agrolavka.ProductsGroup"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8" import="java.util.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="pages" required="true" type="Integer"%>
<%@attribute name="page" required="true" type="Integer"%>
<%@attribute name="view" required="true" type="String"%>
<%@attribute name="sort" required="true" type="String"%>
<%@attribute name="available" required="true" type="Boolean"%>
<%@attribute name="url" required="true" type="String"%>
<%@attribute name="group" required="true" type="ProductsGroup"%>

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
                .append("\"><a class=\"page-link\" href=\"").append(createLink(page, null, null, null))
                .append("\">").append(page).append("</a></li>");
        return sb.toString();
    }
    String createLink(Integer pageParam, String viewParam, String sortParam, Boolean availParam) {
        List<String> params = new ArrayList();
        if (pageParam != null) {
            params.add("page=" + pageParam);
        } else if (page != null && page != 1) {
            params.add("page=" + page);
        }
        if (viewParam != null) {
            params.add("view=" + viewParam);
        } else if (view != null && !view.isEmpty() && !"TILES".equals(view)) {
            params.add("view=" + view);
        }
        if (sortParam != null) {
            params.add("sort=" + sortParam);
        } else if (sort != null && !sort.isEmpty() && !"alphabet".equals(sort)) {
            params.add("sort=" + sort);
        }
        if (availParam != null) {
            params.add("available=" + availParam);
        } else if (availParam == null && available) {
            params.add("available=" + available);
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
<div class="row mb-3 mt-3">
    <div class="col-sm-12 col-lg-12 col-xl-4 d-flex justify-content-start mt-3">
        <c:if test="${pages > 1}">
            <nav aria-label="Page navigation" class="d-flex justify-content-start">
                <ul class="pagination pagination-circle" style="margin-bottom: 0;">
                    <li class="page-item ${page == 1 ? " disabled" : ""}">
                        <a class="page-link" href="<%= createLink(1, null, null, null) %>" aria-label="Первая страница">
                            <i class="fas fa-angle-double-left"></i>
                        </a>
                    </li>
                    <li class="page-item ${page == 1 ? " disabled" : ""}">
                        <a class="page-link" href="<%= createLink(page - 1, null, null, null) %>" aria-label="Назад">
                            <i class="fas fa-angle-left"></i>
                        </a>
                    </li>
                    <%= outputPagination() %>
                    <li class="page-item ${page == pages ? " disabled" : ""}">
                        <a class="page-link" href="<%= createLink(page + 1, null, null, null) %>" aria-label="Вперед">
                            <i class="fas fa-angle-right"></i>
                        </a>
                    </li>
                    <li class="page-item ${page == pages ? " disabled" : ""}">
                        <a class="page-link" href="<%= createLink(pages, null, null, null) %>" aria-label="Последняя страница">
                            <i class="fas fa-angle-double-right"></i>
                        </a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
    <div class="col-sm-12 col-lg-6 col-xl-4 d-flex justify-content-start mt-3">
        <% if (group != null && !AppCache.isBelongsToGroup("Средства защиты растений (СЗР)", group)) { %>
        <div class="dropdown w-100">
            <button class="btn btn-primary dropdown-toggle w-100 text-left" type="button" id="agr-dropdown-products-available" 
                    data-mdb-toggle="dropdown" aria-expanded="false">
                <c:choose>
                    <c:when test="${available}"><i class="fas fa-boxes me-2"></i> только в наличии</c:when>
                    <c:when test="${!available}"><i class="fas fa-box-open me-2"></i> в наличии и под заказ</c:when>
                </c:choose>
            </button>
            <ul class="dropdown-menu w-100" aria-labelledby="agr-dropdown-products-available">
                <li><a class="dropdown-item" href="<%= createLink(1, null, null, false) %>">
                        <i class="fas fa-boxes me-2"></i> в наличии и под заказ</a></li>
                <li><a class="dropdown-item" href="<%= createLink(1, null, null, true) %>">
                        <i class="fas fa-box-open me-2"></i> только в наличии</a></li>
            </ul>
        </div>
        <%
            }
        %>
    </div>
    <div class="col-sm-12 col-lg-6 col-xl-4 d-flex justify-content-end mt-3">
        <div class="dropdown me-2 w-100">
            <button class="btn btn-primary dropdown-toggle w-100 text-left" type="button" id="sort-products" 
                    data-mdb-toggle="dropdown" aria-expanded="false">
                <c:choose>
                    <c:when test="${sort == 'alphabet'}"><i class="fas fa-sort-alpha-up me-2"></i> по алфавиту</c:when>
                    <c:when test="${sort == 'cheap'}"><i class="fas fa-sort-numeric-up me-2"></i> сначала дешевые</c:when>
                    <c:when test="${sort == 'expensive'}"><i class="fas fa-sort-numeric-down me-2"></i> сначала дорогие</c:when>
                </c:choose>
            </button>
            <ul class="dropdown-menu w-100" aria-labelledby="sort-products">
                <li><a class="dropdown-item" href="<%= createLink(null, null, "alphabet", null) %>">
                        <i class="fas fa-sort-alpha-up me-2"></i> по алфавиту</a></li>
                <li><a class="dropdown-item" href="<%= createLink(null, null, "cheap", null) %>">
                        <i class="fas fa-sort-numeric-up me-2"></i> сначала дешевые</a></li>
                <li><a class="dropdown-item" href="<%= createLink(null, null, "expensive", null) %>">
                        <i class="fas fa-sort-numeric-down me-2"></i> сначала дорогие</a></li>
            </ul>
        </div>
        <div class="btn-group">
            <a href="<%= createLink(null, "TILES", null, null) %>" class="btn ${"TILES".equals(view) ? "btn-outline-primary active" : "btn-primary"}">
                <i class="fas fa-th"></i>
            </a>
            <a href="<%= createLink(null, "LIST", null, null) %>" class="btn ${"LIST".equals(view) ? "btn-outline-primary active" : "btn-primary"}">
                <i class="fas fa-list"></i>
            </a>
        </div>
    </div>
</div>
