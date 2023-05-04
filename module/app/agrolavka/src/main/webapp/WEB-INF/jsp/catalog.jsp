<%-- 
    Document   : catalog
    Created on : Feb 18, 2021, 9:04:42 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="modal" tagdir="/WEB-INF/tags/modal" %>

<t:app title="${title}" metaDescription="${metaDescription}" canonical="${canonical}">
    
    <jsp:body>
        <main class="min-vh-100">
            <div class="container mb-5">
                
                <section>
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
                </section>
                <c:if test="${empty categories}">
                    <section class="pb-4">
                        <t:products-search-result searchResult="${searchResult}" pages="${searchResultPages}"
                                                      page="${page}" view="${view}" sort="${sort}" group="${group}" cart="${cart}">
                        </t:products-search-result>
                    </section>
                </c:if>
                
            </div>
        </main>
        <t:nav-back-btn></t:nav-back-btn>
        <modal:one-click-order-modal/>
        <modal:add-to-cart-modal/>
    </jsp:body>

</t:app>
