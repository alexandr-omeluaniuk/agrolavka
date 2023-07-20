<%-- 
    Document   : welcome
    Created on : Feb 14, 2021, 1:19:43 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="modal" tagdir="/WEB-INF/tags/modal" %>

<t:app title="Агролавка | ${title}" canonical=""
       metaDescription="Большой выбор семян, удобрений, средств для защиты растений. Комплектующие для капельного полива. Зоотовары. Приглашаем за покупками.">

    <jsp:attribute name="headSection">
        <link rel="stylesheet" href="/assets/vendor/swiper/swiper-bundle.min.css"/>
        <script src="/assets/vendor/swiper/swiper-bundle.min.js"></script>
    </jsp:attribute>
    
    <jsp:body>
        <main class="min-vh-100" id="home">
            <t:intro visible="true" slides="${slides}"/>
            <div class="container pt-5">

                <!--Section: Content-->
                <t:top-product-groups topCategories="${topCategories}"/>
                
                <c:if test="${productsWithDiscount.size() > 0}">
                    <hr class="my-5" />
                    <t:discounts-highlight products="${productsWithDiscount}"/>
                </c:if>
                    
                <c:if test="${newProducts.size() > 0}">
                    <hr class="my-5" />
                    <t:new_products cart="${cart}" newProducts="${newProducts}"/>
                </c:if>
                
                <hr class="my-5" />
                <!--Section: Content-->
                <section class="mb-4">
                    <div class="row">
                        <div class="col-md-6 gx-4 mb-4">
                            <div class="bg-image hover-overlay ripple shadow-2-strong" data-mdb-ripple-color="light">
                                <img src="/assets/img/agrolavka-location.webp" class="img-fluid"
                                     alt="Фасад магазина Агролавка" height="100"/>
                                <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                            </div>
                        </div>

                        <div class="col-md-6 gx-4">
                            <h4><strong>О магазине Агролавка</strong></h4>
                            <p class="text-muted">
                                Магазин Агролавка – это самые востребованные товары, которые делают жизнь комфортнее, приятнее и радостнее. 
                                У нас широко представлены всевозможные товары для приусадебного участка и дачи, 
                                товары для дома и конечно же товары для настоящих фермеров.
                            </p>
                            <p class="text-muted">
                                У нас вы можете приобрести самые современные системы капельного полива, орошения, удобрения, 
                                средства для защиты растений от насекомых и других вредителей.
                            </p>
                            <p class="text-muted">
                                Продажа товаров для сада и огорода – одно из главных направлений деятельности магазина Agrolavka.by. 
                                Мы предлагаем широкий ассортимент товаров по уходу за приусадебным участком, обработке почвы, 
                                защиты растений от болезней. У нас вы можете купить современные виды удобрений и грунтов, 
                                которые позволят вашим растениям порадовать вас хорошим урожаем!
                            </p>
                        </div>
                    </div>
                    
                </section>
                
                <!--Section: Content-->
            </div>
        </main>
        <modal:one-click-order-modal/>
        <modal:add-to-cart-modal/>
    </jsp:body>

</t:app>