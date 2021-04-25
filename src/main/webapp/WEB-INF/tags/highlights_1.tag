<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@tag description="Product highlights" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<div class="container h-100">
    <div class="row d-flex justify-content-center">
        <div class="col-lg-12 intro-info order-lg-first order-last">
            <h1 class="text-center">Большой ассортимент товаров для сада и огорода!</h1>
        </div>
    </div>
    <div class="row d-flex justify-content-center">
        <div class="col-sm-12 intro-info order-lg-first order-last">
            <h3 class="text-center">Более <span class="purecounter fw-bold" data-purecounter-end="${productsCount}"></span> наименований, приглашаем за покупками!</h3>
            <hr/>
            <div class="row">
                <div class="col-md-3">
                    <img src="/assets/img/highlights/2.jpg" class="rounded mx-auto d-block agr-highlight-img" alt="Большой выбор семян в магазине Агролавка">
                </div>
                <div class="col-md-3">
                    <img src="/assets/img/highlights/3.jpg" class="rounded mx-auto d-block agr-highlight-img" alt="Большой выбор семян в магазине Агролавка">
                </div>
                <div class="col-md-3">
                    <img src="/assets/img/highlights/4.jpg" class="rounded mx-auto d-block agr-highlight-img" alt="Большой выбор удобрений в магазине Агролавка">
                </div>
                <div class="col-md-3">
                    <img src="/assets/img/highlights/5.jpg" class="rounded mx-auto d-block agr-highlight-img" alt="Большой выбор семян в магазине Агролавка">
                </div>
            </div>
            <hr/>
            <div class="row">
                <div class="col-sm-12 col-md-4 align-items-center d-flex flex-column">
                    <div class="agr-brand-text d-flex align-items-center justify-content-start">
                        <div class="agr-highlight-icon"><i class="fas fa-dollar-sign fw-bold fs-4"></i></div>
                        <h4 class="fw-bold">Выгодные цены</h4>
                    </div>
                    <ul>
                        <li>Оптовые скидки и акции</li>
                        <li>Дисконтная программа</li>
                        <li>Скидки постоянным покупателям</li>
                    </ul>
                </div>
                <div class="col-sm-12 col-md-4 align-items-center d-flex flex-column">
                    <div class="agr-brand-text fw-bold d-flex align-items-center justify-content-start">
                        <div class="agr-highlight-icon"><i class="fas fa-star fw-bold fs-4"></i></div>
                        <h4 class="fw-bold">100% Гарантия качества</h4>
                    </div>
                    <ul>
                        <li>Проверенные производители</li>
                        <li>Контроль качества товара</li>
                        <li>Наличие сертификатов</li>
                    </ul>
                </div>
                <div class="col-sm-12 col-md-4 align-items-center d-flex flex-column">
                    <div class="agr-brand-text fw-bold d-flex align-items-center justify-content-start">
                        <div class="agr-highlight-icon"><i class="fas fa-thumbs-up fw-bold fs-4"></i></div>
                        <h4 class="fw-bold">Отличный сервис</h4>
                    </div>
                    <ul>
                        <li>Большой ассортимент</li>
                        <li>Вежливые продавцы</li>
                        <li>Доставка по Беларуси</li>
                    </ul>
                </div>
            </div>
            <p style="text-align: center; margin-top: 30px" class="text-muted">
                На данный момент сайт находится в стадии разработки и в ближайшее время функционал будет расширен и улучшен.
            </p>
        </div>
    </div>
</div>