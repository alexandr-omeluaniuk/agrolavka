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
        
        <div id="subheader-top" class="collapse show">
            <div class="d-flex align-items-center p-2 justify-content-between" style="width: 100%; padding-bottom: 0 !important;">
                <div class="d-flex align-items-center">
                    <a href="tel:+375298713758"><i class="fas fa-phone-alt"></i> +375 29 871-37-58 (МТС)</a>
                    <div class="ms-3 d-none d-xl-block">
                        <small class="text-muted"><b>пн-пт</b> <span class="text-dark">09:00 - 18:30</span></small>,
                        <small class="text-muted"><b>сб</b> <span class="text-dark">09:00 - 16:00</span></small>,
                        <small class="text-muted"><b>вс</b> <span class="text-dark">10:00 - 14:00</span></small>
                    </div>
                </div>
                <div class="d-flex align-items-center">
                    <nav id="navbar" class="navbar order-last order-lg-0">
                        <ul>
                            <li>
                                <a class="nav-link" href="/">
                                    <span><i class="fas fa-home nav-icon"></i> Главная</span>
                                </a>
                            </li>
                            <li>
                                <a class="nav-link" href="/catalog">
                                    <span><i class="fas fa-seedling nav-icon"></i> Наша продукция</span>
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
                    <div class="social-links d-none d-lg-flex align-items-center">
                        <a href="https://www.instagram.com/agrolavka.by"><i class="agr-nav-icon-link fab fa-instagram"></i></a>
                    </div>
                </div>
            </div>
        </div>

        <div class="d-flex align-items-center p-2" style="width: 100%;">
            <h1 class="logo me-auto" id="agrolavka-brand">
                <a href="/" class="d-flex align-items-center">
                    <i class="fas fa-carrot agrolavka-logo"></i><span class="d-none d-xl-block">Агролавка</span>
                </a>
            </h1>        
            <t:products-search></t:products-search>
            <!--a href="/cart" class="btn btn-success ms-3 d-flex align-items-center" style="font-family: inherit;">
                <i class="fas fa-shopping-cart" style="position: relative;">
                    <span class="badge rounded-pill agr-cart-badge">${cart.positions.size()}</span>
                </i>
                <span class="text-light fw-bold ms-2" data-cart-price>
                    ${totalInteger}.<small>${totalDecimal}</small>
                </span>
            </a-->
        </div>
            
        <t:top-product-groups></t:top-product-groups>

    </div>
</header>