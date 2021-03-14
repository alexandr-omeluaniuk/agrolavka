<%-- 
    Document   : navbar
    Created on : Feb 14, 2021, 1:58:06 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix = "cmp" uri = "/WEB-INF/tlds/components.tld"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<header id="header" class="fixed-top d-flex align-items-center header-transparent">
    <div class="container d-flex align-items-center">

        <h1 class="logo me-auto d-none d-lg-block" id="agrolavka-brand">
            <a href="/" class="d-flex align-items-center">
                <i class="fas fa-carrot agrolavka-logo"></i> Агролавка
            </a>
        </h1>        
        <t:products-search></t:products-search>

        <nav id="navbar" class="navbar order-last order-lg-0">
            <ul>
                <li>
                    <a class="nav-link" href="/">
                        <span><i class="fas fa-home nav-icon"></i> Главная</span>
                    </a>
                </li>
                <li class="dropdown">
                    <a href="/catalog" id="mobile-catalog">
                        <span><i class="fas fa-seedling nav-icon"></i> Наша продукция</span> <i class="bi bi-chevron-down"></i>
                    </a>
                    <cmp:menu-catalog></cmp:menu-catalog>
                </li>
                <!--li>
                    <a class="nav-link" href="/about">
                        <span><i class="fas fa-store nav-icon"></i> О нас</span>
                    </a>
                </li-->
                <li>
                    <a class="nav-link scrollto" href="#footer">
                        <span><i class="fas fa-phone-alt nav-icon"></i> Контакты</span>
                    </a></li>
            </ul>
            <i class="bi bi-list mobile-nav-toggle"></i>
        </nav>

        <div class="social-links d-none d-lg-block">
            <!--a href="#" class="twitter"><i class="bi bi-twitter"></i></a>
            <a href="#" class="facebook"><i class="bi bi-facebook"></i></a>
            <a href="#" class="linkedin"><i class="bi bi-linkedin"></i></a-->
            <a href="https://www.instagram.com/agrolavka.by"><i class="fab fa-instagram social-icon"></i></a>
        </div>

    </div>
</header>