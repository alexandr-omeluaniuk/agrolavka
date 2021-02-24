<%-- 
    Document   : product
    Created on : Feb 23, 2021, 10:23:56 AM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cmp" uri="/WEB-INF/tlds/components.tld"%>

<t:app title="Агролавка | ${title}">
    <main id="main">
        <section id="products" class="services" style="padding-top: 120px;">
            <div class="container">
                <header class="section-header">
                    <h3>${title}</h3>
                    <p class="text-uppercase"></p>
                </header>
                <div class="row justify-content-center" style="width: 100%">
                    <div class="col-lg-3 col-md-12 intro-info order-lg-first order-last catalog-desktop">
                        <cmp:catalog groupId="${groupId}"></cmp:catalog>
                    </div>
                    
                    <div class="col-lg-3 col-md-12 intro-info order-lg-first order-last catalog-mobile">
                        <button class="btn btn-outline-info" style="width: 100%" id="mobile-catalog-button">
                            <span><i class="fas fa-seedling nav-icon"></i> Каталог товаров</span></i>
                        </button>
                    </div>

                    <div class="col-lg-9 col-md-12 intro-info order-lg-first order-last">
                        <t:breadcrumb label="${breadcrumbLabel}" groups="${breadcrumbPath}"></t:breadcrumb>
                        <t:product-details product="${product}"></t:product-details>
                    </div>
                </div>
            </div>
        </section>
    </main>
</t:app>
