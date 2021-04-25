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
        <main class="mt-5">
            <div class="container">
                <!--Section: Content-->
                <section>
                    <div class="row">
                        <div class="col-md-6 gx-5 mb-4">
                            <div class="bg-image hover-overlay ripple shadow-2-strong" data-mdb-ripple-color="light">
                                <img src="/assets/img/agrolavka-location.jpg" class="img-fluid" />
                                <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                            </div>
                        </div>

                        <div class="col-md-6 gx-5 mb-4">
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

                <hr class="my-5" />

                <!--Section: Content-->
                <section class="text-center">
                    <h4 class="mb-5"><strong>Facilis consequatur eligendi</strong></h4>

                    <div class="row">
                        <div class="col-lg-4 col-md-12 mb-4">
                            <div class="card">
                                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                                    <img
                                        src="https://mdbootstrap.com/img/new/standard/nature/184.jpg"
                                        class="img-fluid"
                                        />
                                    <a href="#!">
                                        <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                                    </a>
                                </div>
                                <div class="card-body">
                                    <h5 class="card-title">Card title</h5>
                                    <p class="card-text">
                                        Some quick example text to build on the card title and make up the bulk of the
                                        card's content.
                                    </p>
                                    <a href="#!" class="btn btn-primary">Button</a>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4 col-md-6 mb-4">
                            <div class="card">
                                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                                    <img
                                        src="https://mdbootstrap.com/img/new/standard/nature/023.jpg"
                                        class="img-fluid"
                                        />
                                    <a href="#!">
                                        <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                                    </a>
                                </div>
                                <div class="card-body">
                                    <h5 class="card-title">Card title</h5>
                                    <p class="card-text">
                                        Some quick example text to build on the card title and make up the bulk of the
                                        card's content.
                                    </p>
                                    <a href="#!" class="btn btn-primary">Button</a>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4 col-md-6 mb-4">
                            <div class="card">
                                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                                    <img
                                        src="https://mdbootstrap.com/img/new/standard/nature/111.jpg"
                                        class="img-fluid"
                                        />
                                    <a href="#!">
                                        <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                                    </a>
                                </div>
                                <div class="card-body">
                                    <h5 class="card-title">Card title</h5>
                                    <p class="card-text">
                                        Some quick example text to build on the card title and make up the bulk of the
                                        card's content.
                                    </p>
                                    <a href="#!" class="btn btn-primary">Button</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
                <!--Section: Content-->
            </div>
        </main>
    </jsp:body>

</t:app>