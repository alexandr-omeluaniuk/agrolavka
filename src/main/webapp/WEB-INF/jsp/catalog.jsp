<%-- 
    Document   : catalog
    Created on : Feb 18, 2021, 9:04:42 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="Агролавка | ${title}" metaDescription="${group.name}">
    
    <jsp:body>
        <main id="main">
            <section id="products" class="services">
                <div class="container">
                    <header class="section-header">
                        <h3>Наша продукция</h3>
                        <c:choose>
                            <c:when test="${title}">
                                <p class="text-uppercase text-muted">Товары для сада и огорода</p>
                            </c:when>    
                            <c:otherwise>
                                <p class="text-uppercase text-muted">${title}</p>
                            </c:otherwise>
                        </c:choose>

                    </header>
                    <div class="row justify-content-center">
                        <div class="col-lg-3 col-md-12 intro-info order-lg-first order-last">
                            <t:product-groups-tree groups="${groups}" groupId="${group.id}"></t:product-groups-tree>
                        </div>

                        <div class="col-lg-9 col-md-12 intro-info order-lg-first order-last">
                            <t:breadcrumb label="${breadcrumbLabel}" groups="${breadcrumbPath}"></t:breadcrumb>
                            <t:products-search-result searchResult="${searchResult}" pages="${searchResultPages}"
                                                      page="${page}" view="${view}" group="${group}">

                            </t:products-search-result>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    </jsp:body>

</t:app>
