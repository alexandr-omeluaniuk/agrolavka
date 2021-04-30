<%-- 
    Document   : navbar
    Created on : Feb 14, 2021, 1:58:06 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<header>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark agr-navbar shadow-2-strong">
        <div class="container-fluid">
            <!-- Navbar brand -->
            <a href="/" class="navbar-brand nav-link d-none d-lg-block">
                <strong class="d-flex justify-content-center align-items-center">
                    <i class="fas fa-carrot"></i><span class="ms-2">Агролавка</span>
                </strong>
            </a>
            <div class="d-lg-none w-100">
                <div class="arg-top-navbar">
                    <div class="d-flex align-items-center justify-content-between mb-2">
                        <a class="nav-link" href="tel:+375298713758"><i class="fas fa-phone-alt"></i> +375 29 871-37-58 (МТС)</a>
                        <button class="navbar-toggler" type="button" data-mdb-toggle="collapse"
                                data-mdb-target="#agr-navbar" aria-controls="agr-navbar" aria-expanded="false" aria-label="Toggle navigation">
                            <i class="fas fa-bars"></i>
                        </button>
                    </div>
                </div>
                <div class="d-flex align-items-center justify-content-between">
                    <t:quick-search-mobile />
                    <t:cart cart="${cart}" totalDecimal="${totalDecimal}" totalInteger="${totalInteger}"/>
                </div>
            </div>
            <div class="collapse navbar-collapse" id="agr-navbar">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item active">
                        <a class="nav-link" aria-current="page" href="/">Главная</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/catalog">Каталог товаров</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/delivery">Доставка</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#footer">Контакты</a>
                    </li>
                </ul>
                <div class="d-none d-lg-flex">
                    <t:quick-search-desktop />
                    <t:cart cart="${cart}" totalDecimal="${totalDecimal}" totalInteger="${totalInteger}"/>
                </div>
                <ul class="navbar-nav d-flex flex-row">
                    <!-- Icons -->
                    <li class="nav-item me-3 me-lg-0">
                        <a class="nav-link d-flex align-items-center" href="https://www.instagram.com/agrolavka.by" target="_blank">
                            <i class="fab fa-instagram"></i><span class="ms-2 d-block d-lg-none">Инстаграм</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <!-- Navbar -->
</header>
