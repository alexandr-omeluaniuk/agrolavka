<%-- 
    Document   : navbar
    Created on : Feb 14, 2021, 1:58:06 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<header class="agr-navbar-mobile">
    <nav class="navbar navbar-expand-lg navbar-dark agr-navbar shadow-2-strong">
        <div class="container-fluid">
            <div class="d-lg-none w-100">
                <div class="arg-top-navbar">
                    <div class="d-flex align-items-center mb-2">
                        <a href="/" class="navbar-brand nav-link" style="border-right: 1px solid #ffffff70; margin-right: 0;">
                            <strong class="d-flex justify-content-center align-items-center">
                                <i class="fas fa-carrot agr-carrot-logo"></i>
                            </strong>
                        </a>
                        <a class="nav-link" href="tel:+375292848848" style="flex: 1"><i class="fas fa-phone-alt"></i> +375 29 2-848-848 (МТС)</a>

                    </div>
                </div>
                <div class="d-flex align-items-center justify-content-between">
                    <button class="navbar-toggler agr-mobile-menu-btn" type="button">
                        <i class="fas fa-bars"></i>
                    </button>
                    <t:quick-search-mobile />
                    <t:cart cart="${cart}" totalDecimal="${totalDecimal}" totalInteger="${totalInteger}"/>
                </div>
            </div>
            <!--div class="collapse navbar-collapse justify-content-end" id="agr-mm-navbar-menu">
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
                <ul class="navbar-nav d-flex flex-row">
                    <li class="nav-item me-3 ms-3 me-lg-0">
                        <a class="nav-link d-flex align-items-center" href="https://www.instagram.com/agrolavka.by" target="_blank" rel="noreferrer">
                            <img src="/assets/img/instagram.ico" alt="Instagram"><span class="ms-2 d-block d-lg-none">Инстаграм</span>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link d-flex align-items-center" href="https://invite.viber.com/?g2=AQAg5Rkk2LluF0zHtRAvabtjZ4jDtGaaMApRoqe3%2FboHZogbep9nBgCTSKDPVqTl" target="_blank" rel="noreferrer">
                            <i class="fab fa-viber" style="font-size: 32px;"></i><span class="ms-2 d-block d-lg-none">Вайбер</span>
                        </a>
                    </li>
                </ul>
            </div-->
        </div>
    </nav>

    <nav class="agr-menu-sidebar shadow-2-strong">
        <div class="d-flex align-items-center shadow-2 p-3">
            <a href="/">
                <h3 class="mb-0"><i class="fas fa-carrot agr-carrot-logo"></i></h3>
            </a>
            <h4 class="text-center mb-0" style="flex: 1">Агролавка</h4>
            <button class="navbar-toggler agr-mobile-menu-close-btn" type="button">
                <h3 class="mb-0"><i class="fas fa-chevron-left"></i></h3>
            </button>
        </div>
        <div class="position-sticky">
            <div class="list-group list-group-flush mx-3 mt-4">
                <a href="/" class="list-group-item list-group-item-action py-2 ripple" aria-current="true">
                    <i class="fas fa-home fa-fw me-3"></i><span>Главная</span>
                </a>
                <a href="/catalog" class="list-group-item list-group-item-action py-2 ripple" aria-current="true">
                    <i class="fas fa-box-open fa-fw me-3"></i><span>Каталог</span>
                </a>
                <a href="/promotions" class="list-group-item list-group-item-action py-2 ripple text-danger" aria-current="true">
                    <i class="fas fas fa-fire fa-fw me-3"></i><span>Акции</span>
                </a>
                <a href="/delivery" class="list-group-item list-group-item-action py-2 ripple" aria-current="true">
                    <i class="fas fa-truck fa-fw me-3"></i><span>Доставка</span>
                </a>
                <a href="/discount" class="list-group-item list-group-item-action py-2 ripple" aria-current="true">
                    <i class="fas fa-percent fa-fw me-3"></i><span>Дисконтная программа</span>
                </a>
                <a href="#contacts" class="list-group-item list-group-item-action py-2 ripple" aria-current="true" style="border-top: 2px solid #dedede;">
                    <i class="fas fa-map-marker-alt fa-fw me-3"></i><span>Контакты</span>
                </a>
                <a href="/feedback" class="list-group-item list-group-item-action py-2 ripple" aria-current="true">
                    <i class="fas fa-comment fa-fw me-3"></i><span>Написать нам</span>
                </a>
            </div>
        </div>
    </nav>

</header>
<div class="agr-backdrop"></div>

