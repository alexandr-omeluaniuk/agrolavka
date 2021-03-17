<%-- 
    Document   : welcome
    Created on : Feb 14, 2021, 1:19:43 PM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cmp" uri="/WEB-INF/tlds/components.tld"%>

<t:app title="Агролавка | ${title}">

    <section id="hero" class="clearfix">
        <div class="container h-100">
            <div class="row d-flex justify-content-center">
                <div class="col-lg-12 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">
                    <h2 class="text-center">Самый большой ассортимент<br>товаров для сада и огорода!</h2>
                </div>
            </div>
            <div class="row d-flex justify-content-center" data-aos="fade-up" style="width: 100%">
                <div class="col-lg-6 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">
                    <header class="section-header">
                        <h3 class="text-center">Более <span class="purecounter" data-purecounter-end="${productsCount}"></span> наименований!</h3>
                        <h4 class="text-center">Большое поступление семян овощных и цветочных культур, элитные голландские обработанные семена!</h4>
                    </header>
                    <p style="text-align: center; margin-top: 30px" class="text-muted">
                        На данный момент сайт находится в стадии разработки и в ближайшее время будет выглядеть наилучшим образом.
                    </p>
                </div>

                <div class="col-lg-6 intro-info order-lg-first order-last d-flex justify-content-end"
                        data-aos="zoom-in" data-aos-delay="100">
                    <t:highlights/>
                </div>
            </div>
    </section>
    <!--main id="main">
        <section>
            
        </section>
    </main-->

</t:app>