<%-- 
    Document   : navbar
    Created on : Feb 14, 2021, 1:58:06 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix = "cmp" uri = "/WEB-INF/tlds/components.tld"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="activePage" required="true"%>

<%-- any content can be specified here e.g.: --%>
<header id="header" class="fixed-top d-flex align-items-center header-transparent">
    <div class="container d-flex align-items-center">

        <h1 class="logo me-auto"><a href="/"><i class="fas fa-carrot agrolavka-logo"></i> Агролавка</a></h1>
        <!-- Uncomment below if you prefer to use an image logo -->
        <!-- <a href="index.html" class="logo me-auto"><img src="assets/img/logo.png" alt="" class="img-fluid"></a>-->

        <nav id="navbar" class="navbar order-last order-lg-0">
            <ul>
                <li><a class="nav-link scrollto ${activePage == "HOME" ? "active" : ""}" href="/">Главная</a></li>
                <li class="dropdown">
                    <a href="/catalog" class="${activePage == "PRODUCTS" ? "active" : ""}">
                        <span>Наша продукция</span> <i class="bi bi-chevron-down"></i>
                    </a>
                    <cmp:menu-catalog></cmp:menu-catalog>
                </li>
                <li><a class="nav-link scrollto ${activePage == "ABOUT" ? "active" : ""}" href="/about">О нас</a></li>
                <li><a class="nav-link scrollto" href="#footer">Контакты</a></li>
            </ul>
            <i class="bi bi-list mobile-nav-toggle"></i>
        </nav><!-- .navbar -->

        <div class="social-links">
            <!--a href="#" class="twitter"><i class="bi bi-twitter"></i></a>
            <a href="#" class="facebook"><i class="bi bi-facebook"></i></a>
            <a href="#" class="linkedin"><i class="bi bi-linkedin"></i></a-->
            <a href="https://www.instagram.com/agrolavka.by" class="instagram"><i class="bi bi-instagram"></i></a>
        </div>

    </div>
</header>