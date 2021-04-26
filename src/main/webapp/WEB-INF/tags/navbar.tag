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
    <nav class="navbar navbar-expand-lg navbar-dark" style="z-index: 2000;">
        <div class="container-fluid">
            <!-- Navbar brand -->
            <a href="/" class="navbar-brand nav-link">
                <strong class="d-flex justify-content-center align-items-center">
                    <i class="fas fa-carrot"></i><span class="ms-2">Агролавка</span>
                </strong>
            </a>
            <button class="navbar-toggler" type="button" data-mdb-toggle="collapse"
                    data-mdb-target="#navbarExample01" aria-controls="navbarExample01" aria-expanded="false" aria-label="Toggle navigation">
                <i class="fas fa-bars"></i>
            </button>
            <div class="collapse navbar-collapse" id="navbarExample01">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item active">
                        <a class="nav-link" aria-current="page" href="/">Главная</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/catalog">Каталог товаров</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#footer">Контакты</a>
                    </li>
                </ul>
                <t:products-search />
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

    <t:intro/>
</header>
