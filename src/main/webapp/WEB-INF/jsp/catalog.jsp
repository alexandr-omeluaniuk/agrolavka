<%-- 
    Document   : catalog
    Created on : Feb 18, 2021, 9:04:42 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="${title}" metaDescription="${metaDescription}" canonical="${canonical}">
    
    <jsp:body>
        <main class="mb-5 mt-5 pt-5 min-vh-100">
            <div class="container">
                <h3 class="text-center mb-4">${group != null ? group.name : 'Каталог товаров'}</h3>
                <c:choose>
                    <c:when test="${group != null}">
                        <p class="text-muted text-justify">${group.description}</p>
                    </c:when>    
                    <c:otherwise>
                        <p class="text-uppercase text-muted text-center">Товары для сада и огорода</p>
                    </c:otherwise>
                </c:choose>
                <t:breadcrumb label="${breadcrumbLabel}" groups="${breadcrumbPath}"/>
                <t:categories-grid categories="${categories}" />
            </div>
        </main>
    </jsp:body>

</t:app>
