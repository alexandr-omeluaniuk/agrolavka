<%-- 
    Document   : navbar
    Created on : Feb 14, 2021, 1:58:06 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<header id="header" class="fixed-top d-flex align-items-center header-transparent">
    <div class="container d-flex flex-column">

        <div class="d-flex align-items-center p-2" style="width: 100%;">
            <h1 class="logo me-auto" id="agrolavka-brand">
                <a href="/" class="d-flex align-items-center">
                    <i class="fas fa-carrot agrolavka-logo"></i><span class="d-none d-xl-block">Агролавка</span>
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
                    <li>
                        <a class="nav-link" href="/catalog">
                            <span><i class="fas fa-seedling nav-icon"></i> Наша продукция</span></i>
                        </a>
                    </li>
                    <li>
                        <a class="nav-link scrollto" href="#footer">
                            <span><i class="fas fa-phone-alt nav-icon"></i> Контакты</span>
                        </a>
                    </li>
                </ul>
                <i class="bi bi-list mobile-nav-toggle"></i>
            </nav>
            <div class="social-links d-none d-lg-block">
                <a href="https://www.instagram.com/agrolavka.by"><i class="fab fa-instagram social-icon"></i></a>
            </div>
        </div>
            
        <t:top-product-groups></t:top-product-groups>

    </div>
</header>