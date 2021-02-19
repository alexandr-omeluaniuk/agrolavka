<%-- 
    Document   : catalog
    Created on : Feb 18, 2021, 9:04:42 PM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cmp" uri="/WEB-INF/tlds/components.tld"%>

<t:app title="Агролавка | ${title}" activePage="PRODUCTS">
    <main id="main">
        <section id="products" class="services" style="padding-top: 180px;">
            <div class="container" data-aos="fade-up">
                <header class="section-header">
                    <h3>Наша продукция</h3>
                    <p>ТОВАРЫ ДЛЯ САДА И ОГОРОДА</p>
                </header>
                <div class="row justify-content-center" data-aos="fade-up" style="width: 100%">
                    <div class="col-lg-4 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">
                        <cmp:catalog></cmp:catalog>
                        </div>

                        <div class="col-lg-8 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">
                        <cmp:search-result-tag groupId="${groupId}"></cmp:search-result-tag>
                        </div>
                    </div>
                </div>
            </section>
        </main>

</t:app>
