<%-- 
    Document   : navbar
    Created on : Feb 14, 2021, 1:58:06 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<header class="agr-navbar-desktop">
    <nav class="navbar navbar-expand-lg navbar-dark agr-navbar shadow-2-strong">
        <div class="container-fluid">
            <a href="/" class="navbar-brand nav-link d-none d-lg-block">
                <strong class="d-flex justify-content-center align-items-center">
                    <i class="fas fa-carrot agr-carrot-logo" style="font-size: 1.5em;"></i><span class="ms-2">Агролавка</span>
                </strong>
            </a>
            <div class="navbar-collapse justify-content-end">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item active">
                        <a class="nav-link" aria-current="page" href="/">Главная</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/catalog">Каталог</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/promotions">Акции</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/delivery">Доставка</a>
                    </li>
                    <li class="nav-item d-lg-none d-xl-none d-xxl-block">
                        <a class="nav-link" href="/discount">Дисконтная программа</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#contacts">Контакты</a>
                    </li>
                    <li class="nav-item d-lg-none d-xl-none d-xxl-none">
                        <a class="nav-link" href="/feedback">Написать нам</a>
                    </li>
                </ul>
                <div class="d-none d-lg-flex align-items-center me-4">
                    <a class="text-white" href="tel:+375292848848"><i class="fas fa-phone-alt"></i> <b>(29) 2-848-848</b></a><br>
                </div>
                <div class="d-none d-lg-flex">
                    <t:quick-search-desktop />
                    <t:cart cart="${cart}" totalDecimal="${totalDecimal}" totalInteger="${totalInteger}"/>
                </div>
                <ul class="navbar-nav d-flex flex-row">
                    <!-- Icons -->
                    <li class="nav-item me-3 ms-3 me-lg-0">
                        <a class="nav-link d-flex align-items-center" href="https://www.instagram.com/agrolavka.by" target="_blank" rel="noreferrer">
                            <img src="/assets/img/instagram.ico" alt="Instagram"><span class="ms-2 d-block d-lg-none">Инстаграм</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link d-flex align-items-center" href="https://invite.viber.com/?g2=AQAg5Rkk2LluF0zHtRAvabtjZ4jDtGaaMApRoqe3%2FboHZogbep9nBgCTSKDPVqTl" target="_blank" rel="noreferrer">
                            <div class="agr-viber">
                                <i class="fab fa-viber"></i>
                            </div>
                            <span class="ms-2 d-block d-lg-none">Вайбер</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="agr-desktop-menu-btn">
        <i class="fas fa-bars"></i>
    </div>
</header>
