<%-- 
    Document   : welcome
    Created on : Feb 14, 2021, 1:19:43 PM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="Агролавка | ${title}" canonical="/"
       metaDescription="Большой выбор семян, удобрений, средств для защиты растений. Комплектующие для капельного полива. Зоотовары. Приглашаем за покупками.">

    <jsp:body>
        <main>
            <div id="agr-highlights" class="carousel carousel-dark slide d-none d-md-block" data-bs-ride="carousel">
                <div class="carousel-indicators">
                    <button type="button" data-bs-target="#agr-highlights" data-bs-slide-to="0" class="active" 
                            aria-current="true" aria-label="Вступление"></button>
                    <button type="button" data-bs-target="#agr-highlights" data-bs-slide-to="1" 
                            aria-current="true" aria-label="Новинки"></button>
                </div>
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <t:highlights/>
                    </div>
                    <div class="carousel-item">
                        <t:new_products newProducts="${newProducts}"/>
                    </div>
                </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#agr-highlights" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#agr-highlights" data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                </button>
            </div>
            <div class="container mb-5" style="margin-top: 7rem">
                <div class="row featurette">
                    <div class="col-md-7">
                        <h2 class="featurette-heading">О магазине Агролавка</h2>
                        <p class="lead">
                            Магазин Агролавка – это самые востребованные товары, которые делают жизнь комфортнее, приятнее и радостнее. 
                            У нас широко представлены всевозможные товары для приусадебного участка и дачи, 
                            товары для дома и конечно же товары для настоящих фермеров.<br/>
                            У нас вы можете приобрести самые современные системы капельного полива, орошения, удобрения, 
                            средства для защиты растений от насекомых и других вредителей.<br/>
                            Продажа товаров для сада и огорода – одно из главных направлений деятельности магазина Agrolavka.by. 
                            Мы предлагаем широкий ассортимент товаров по уходу за приусадебным участком, обработке почвы, 
                            защиты растений от болезней. У нас вы можете купить современные виды удобрений и грунтов, 
                            которые позволят вашим растениям порадовать вас хорошим урожаем!
                        </p>
                    </div>
                    <div class="col-md-5">
                        <img class="img-fluid mx-auto" width="500" height="500" src="/assets/img/agrolavka-location.jpg"
                             alt="Фото магазина Агролавка"/>
                    </div>
                </div>
            </div>
        </main>
    </jsp:body>

</t:app>