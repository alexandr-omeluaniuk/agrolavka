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

        <h1 class="logo me-auto" id="agrolavka-brand"><a href="/"><i class="fas fa-carrot agrolavka-logo"></i> Агролавка</a></h1>
        
        <div class="input-group mb-3" id="products-search-container">
            <input type="search" class="form-control" aria-label="Поиск товаров" id="products-search" placeholder="Поиск">
            <button class="btn btn-outline-info" type="button" id="products-search-close" title="Закрыть поиск">
                <i class="fas fa-chevron-right"></i>
            </button>
        </div>
        <ul class="dropdown-menu shadow-sm" aria-labelledby="products-search-container" id="products-search-results-list"></ul>
        
        <!-- Uncomment below if you prefer to use an image logo -->
        <!-- <a href="index.html" class="logo me-auto"><img src="assets/img/logo.png" alt="" class="img-fluid"></a>-->

        <nav id="navbar" class="navbar order-last order-lg-0">
            <ul>
                <li>
                    <a class="nav-link scrollto ${activePage == "HOME" ? "active" : ""}" href="/">
                        <span><i class="fas fa-home nav-icon"></i> Главная</span>
                    </a>
                </li>
                <li class="d-none d-lg-block">
                    <a class="nav-link" href="#" id="products-search-link">
                        <span><i class="fas fa-search nav-icon"></i> Поиск</span>
                    </a>
                </li>
                <li class="dropdown">
                    <a href="/catalog" class="${activePage == "PRODUCTS" ? "active" : ""}" id="mobile-catalog">
                        <span><i class="fas fa-seedling nav-icon"></i> Наша продукция</span> <i class="bi bi-chevron-down"></i>
                    </a>
                    <cmp:menu-catalog></cmp:menu-catalog>
                </li>
                <!--li>
                    <a class="nav-link scrollto ${activePage == "ABOUT" ? "active" : ""}" href="/about">
                        <span><i class="fas fa-store nav-icon"></i> О нас</span>
                    </a>
                </li-->
                <li>
                    <a class="nav-link scrollto" href="#footer">
                        <span><i class="fas fa-phone-alt nav-icon"></i> Контакты</span>
                    </a></li>
            </ul>
            <i class="bi bi-list mobile-nav-toggle"></i>
        </nav><!-- .navbar -->

        <div class="social-links">
            <a href="#products" id="nav-search-button" class="d-lg-none"><i class="fas fa-search"></i></a>
            <!--a href="#" class="twitter"><i class="bi bi-twitter"></i></a>
            <a href="#" class="facebook"><i class="bi bi-facebook"></i></a>
            <a href="#" class="linkedin"><i class="bi bi-linkedin"></i></a-->
            <a href="https://www.instagram.com/agrolavka.by"><i class="fab fa-instagram social-icon"></i></a>
        </div>

    </div>
</header>