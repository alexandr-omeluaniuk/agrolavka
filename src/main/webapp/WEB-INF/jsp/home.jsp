<%-- 
    Document   : welcome
    Created on : Feb 14, 2021, 1:19:43 PM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cmp" uri="/WEB-INF/tlds/components.tld"%>

<t:app title="Агролавка | ${title}" activePage="HOME">

    <section id="hero" class="clearfix">
        <div class="container h-100">
            <div class="row justify-content-center">
                <div class="col-lg-12 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">
                    <h2>Самый большой ассортимент<br>товаров для сада и огорода в <span>Дрогичине!</span></h2>
                </div>
            </div>
            <div class="row justify-content-center" data-aos="fade-up" style="width: 100%">
                <div class="col-lg-4 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">

                </div>

                <div class="col-lg-8 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">

                </div>
            </div>
    </section>
    <main id="main">
        <section id="products" class="services">
            <div class="container" data-aos="fade-up">
                <header class="section-header">
                    <h3>Наша продукция</h3>
                    <p>ТОВАРЫ ДЛЯ САДА И ОГОРОДА</p>
                </header>
                <div class="row justify-content-center" data-aos="fade-up" style="width: 100%">
                    <div class="col-lg-3 col-md-12 intro-info order-lg-first order-last catalog-desktop">
                        <cmp:catalog groupId="${groupId}"></cmp:catalog>
                    </div>
                    
                    <div class="col-lg-3 col-md-12 intro-info order-lg-first order-last catalog-mobile">
                        <button class="btn btn-outline-info" style="width: 100%" id="mobile-catalog-button">
                            <span><i class="fas fa-seedling nav-icon"></i> Каталог товаров</span></i>
                        </button>
                    </div>

                    <div class="col-lg-8 col-md-12 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">
                        <cmp:search-result-tag groupId="${groupId}" page="${page}" view="${view}" url="/catalog"></cmp:search-result-tag>
                    </div>
                </div>
            </div>
        </section>
    </main>

</t:app>