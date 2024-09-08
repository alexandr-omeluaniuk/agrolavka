<%-- 
    Document   : footer
    Created on : Feb 14, 2021, 3:27:56 PM
    Author     : alex
--%>

<%@tag description="Site footer" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<footer class="bg-dark text-white shadow-2" id="footer">
    <div class="container pt-4">

        <div class="row" id="contacts">
            <c:forEach items="${shops}" var="shop">
                <div class="col-lg-4 col-md-12 gx-4">
                    <t:shop-short-info shop="${shop}"/>
                </div>
            </c:forEach>
        </div>

        <hr class="d-lg-block d-none"/>

        <div class="row">
            <div class="col-lg-4 col-md-12">
                <h3>Агролавка</h3>
                <p>Наш магазин «Агролавка» открылся в феврале 2020 года. Наша главная цель предложить нашим клиентам самый широкий выбор товаров для сада и дачи по самым низким ценам. Мы настроены на долгосрочное сотрудничество и очень надеемся, что Вы станете нашим постоянным клиентом.</p>
                <hr class="d-lg-none d-md-block"/>
            </div>
            <div class="col-lg-4 col-md-12">
                <h4>Полезные ссылки</h4>
                <ul class="list-unstyled">
                    <li class="mb-2"><a class="text-white" href="/">Главная</a></li>
                    <li class="mb-2"><a class="text-white" href="/catalog">Каталог товаров</a></li>
                    <li class="mb-2"><a class="text-white" href="/discount">Дисконтная программа</a></li>
                    <li class="mb-2"><a class="text-white" href="/delivery">Доставка</a></li>
                    <li class="mb-2"><a class="text-white" href="/shops">Наши магазины</a></li>
                    <li class="d-flex gap-3 mb-2 flex-column">
                        <a href="https://www.instagram.com/agrolavka.by">
                            <div class="d-flex align-items-center">
                                <img src="/assets/img/instagram.ico" alt="Instagram">
                                <h5 class="text-light mb-0 mt-0 ms-2">Инстаграм</h5>
                            </div>
                        </a>
                        <a href="https://invite.viber.com/?g2=AQAg5Rkk2LluF0zHtRAvabtjZ4jDtGaaMApRoqe3%2FboHZogbep9nBgCTSKDPVqTl">
                            <div class="d-flex align-items-center">
                                <div class="agr-viber">
                                    <i class="fab fa-viber"></i>
                                </div>
                                <h5 class="text-light mb-0 mt-0 ms-2">Viber</h5>
                            </div>
                        </a>
                    </li>
                </ul>
                <hr class="d-lg-none d-md-block"/>
            </div>

            <div class="col-lg-4 col-md-12">
                <h4>Информация для покупателя</h4>
                <small>
                        <b>Юридическое лицо:</b> ООО «Алском»<br/>
                        <b>Юр.адрес:</b> 224024, г.Брест, Ул.Красногвардейская, 129<br/>
                        <b>Регистрационный номер ЕГР:</b> УНП 291636018<br/>
                        <b>Регистрационный орган:</b> Администрация Ленинского района г.Бреста<br/>
                        <b>Дата регистрации компании:</b> 10.12.2019г.
                </small><br/><br/>
                <strong>Тел:</strong> <a class="text-white" href="tel:+375292848848">+375 (29) 2-848-848</a><br/>
                <strong>Email:</strong> <a class="text-white" href="mailto:sergej4ikk@mail.ru">sergej4ikk@mail.ru</a>
                <hr class="d-lg-none d-md-block"/>
            </div>

        </div>

        <div class="text-center p-3">
            <small>&copy; <strong>Агролавка</strong>. Все права защищены</small>
        </div>

    </div>
</footer>