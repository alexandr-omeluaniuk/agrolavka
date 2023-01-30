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
                <h3 class="mb-0"><i class="fas fa-times"></i></h3>
            </button>
        </div>
        <div class="position-sticky">
            <div class="list-group list-group-flush mx-3 mt-4">
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    aria-current="true"
                    >
                    <i class="fas fa-tachometer-alt fa-fw me-3"></i
                    ><span>Main dashboard</span>
                </a>
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple active"
                    >
                    <i class="fas fa-chart-area fa-fw me-3"></i
                    ><span>Webiste traffic</span>
                </a>
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    ><i class="fas fa-lock fa-fw me-3"></i><span>Password</span></a
                >
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    ><i class="fas fa-chart-line fa-fw me-3"></i
                    ><span>Analytics</span></a
                >
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    >
                    <i class="fas fa-chart-pie fa-fw me-3"></i><span>SEO</span>
                </a>
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    ><i class="fas fa-chart-bar fa-fw me-3"></i><span>Orders</span></a
                >
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    ><i class="fas fa-globe fa-fw me-3"></i
                    ><span>International</span></a
                >
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    ><i class="fas fa-building fa-fw me-3"></i
                    ><span>Partners</span></a
                >
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    ><i class="fas fa-calendar fa-fw me-3"></i
                    ><span>Calendar</span></a
                >
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    ><i class="fas fa-users fa-fw me-3"></i><span>Users</span></a
                >
                <a
                    href="#"
                    class="list-group-item list-group-item-action py-2 ripple"
                    ><i class="fas fa-money-bill fa-fw me-3"></i><span>Sales</span></a
                >
            </div>
        </div>
    </nav>

</header>
<div class="agr-backdrop"></div>

