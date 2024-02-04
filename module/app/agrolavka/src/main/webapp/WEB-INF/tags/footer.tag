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
        <div class="row">
            <div class="col-lg-9 gx-4">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Агролавка</h3>
                        <p>Наш магазин «Агролавка» открылся в феврале 2020 года. Наша главная цель предложить нашим клиентам самый широкий выбор товаров для сада и дачи по самым низким ценам. Мы настроены на долгосрочное сотрудничество и очень надеемся, что Вы станете нашим постоянным клиентом.</p>
                        <!--p>Помогите улучшить наш сайт, напишите нам ваши замечания и предложения</p-->
                        <!--a href="/feedback" class="btn btn-outline-light mb-3" type="button">
                            Написать нам
                        </a-->
                    </div>
                    <div class="col-sm-4">
                        <h4>Полезные ссылки</h4>
                        <ul class="list-unstyled">
                            <li class="mb-2"><a class="text-white" href="/">Главная</a></li>
                            <li class="mb-2"><a class="text-white" href="/catalog">Каталог товаров</a></li>
                            <li class="mb-2"><a class="text-white" href="/discount">Дисконтная программа</a></li>
                            <li class="mb-2"><a class="text-white" href="/delivery">Доставка</a></li>
                            <li class="mb-2"><a class="text-white" href="/shops">Наши магазины</a></li>
                            <li class="d-flex" style="gap: 10px;">
                                <a href="https://www.instagram.com/agrolavka.by">
                                    <img src="/assets/img/instagram.ico" alt="Instagram">
                                </a>
                                <a href="https://invite.viber.com/?g2=AQAg5Rkk2LluF0zHtRAvabtjZ4jDtGaaMApRoqe3%2FboHZogbep9nBgCTSKDPVqTl">
                                    <div class="agr-viber">
                                        <i class="fab fa-viber"></i>
                                    </div>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 gx-4" id="contacts">
                <h4>Контакты</h4>
                <c:forEach items="${shops}" var="shop">
                    <t:shop-short-info shop="${shop}"/>
                </c:forEach>
                <strong>Email:</strong> <a class="text-white" href="mailto:sergej4ikk@mail.ru">sergej4ikk@mail.ru</a><br>
            </div>

        </div>

        <div class="text-center p-3">
            <small>&copy; <strong>Агролавка</strong>. Все права защищены</small>
        </div>

    </div>
</footer>