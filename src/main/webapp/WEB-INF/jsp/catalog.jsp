<%-- 
    Document   : catalog
    Created on : Feb 18, 2021, 9:04:42 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cmp" uri="/WEB-INF/tlds/components.tld"%>

<t:app title="Агролавка | ${title}" activePage="PRODUCTS">
    <main id="main">
        <section id="products" class="services" style="padding-top: 120px;">
            <div class="container">
                <header class="section-header">
                    <h3>Наша продукция</h3>
                    <c:choose>
                        <c:when test="${title}">
                            <p class="text-uppercase">Товары для сада и огорода</p>
                        </c:when>    
                        <c:otherwise>
                            <p class="fs-3 text-uppercase fst-italic">${title}</p>
                        </c:otherwise>
                    </c:choose>
                    
                </header>
                <div class="row justify-content-center" style="width: 100%">
                    <div class="col-lg-3 col-md-12 intro-info order-lg-first order-last catalog-desktop">
                        <cmp:catalog groupId="${groupId}"></cmp:catalog>
                    </div>
                    
                    <div class="col-lg-3 col-md-12 intro-info order-lg-first order-last catalog-mobile">
                        <ul class="navbar navbar-mobile">
                            <li class="dropdown" style="width: 100%">
                                <button class="btn btn-outline-info" style="width: 100%">
                                    <span><i class="fas fa-seedling nav-icon"></i> Каталог товаров</span> <i class="bi bi-chevron-down"></i>
                                </button>
                                <cmp:menu-catalog></cmp:menu-catalog>
                            </li>
                        </ul>
                    </div>

                    <div class="col-lg-9 col-md-12 intro-info order-lg-first order-last">
                        <cmp:search-result-tag groupId="${groupId}" page="${page}" view="${view}"></cmp:search-result-tag>
                    </div>
                </div>
            </div>
        </section>
    </main>

</t:app>
