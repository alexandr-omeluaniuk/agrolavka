<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="groups" required="false" type="java.util.List<ss.entity.agrolavka.ProductsGroup>"%>
<%@attribute name="label" required="false" type="java.lang.String"%>

<%-- any content can be specified here e.g.: --%>
<c:if test="${label != null}">
    <% String divider = "--bs-breadcrumb-divider: url(&#34;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='8' height='8'%3E%3Cpath d='M2.5 0L1 1.5 3.5 4 1 6.5 2.5 8l4-4-4-4z' fill='currentColor'/%3E%3C/svg%3E&#34;);"; %>
    <nav aria-label="breadcrumb" style="<%= divider %>">
        <ol class="breadcrumb">
            <%
                if (groups != null) {
            %>
                <li class="breadcrumb-item">
                    <a href="/catalog" class="agr-link">
                        <small>Каталог</small>
                    </a>
                </li>
            <%
                }
            %>
            <% 
                if (groups != null) {
                    for (ProductsGroup group : groups) { 
                        boolean isLast = groups.indexOf(group) == groups.size() - 1;
                        final String linkId = isLast ? "id=\"agr-last-catalog-link\"" : "";
            %>
                        <li class="breadcrumb-item">
                            <a href="<%= UrlProducer.buildProductGroupUrl(group) %>" class="agr-link" <%= linkId %>>
                                <small><%= group.getName() %></small>
                            </a>
                        </li>
            <%
                    }
                }
            %>
            <li class="breadcrumb-item active" aria-current="page">
                <small>${label}</small>
            </li>
        </ol>
    </nav>
</c:if>