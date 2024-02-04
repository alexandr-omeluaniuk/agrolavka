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
                <div class="arg-top-navbar mb-2">
                    <div class="d-flex align-items-center">
                        <a href="/" class="navbar-brand nav-link" style="border-right: 1px solid #ffffff70; margin-right: 0;">
                            <strong class="d-flex justify-content-center align-items-center">
                                <i class="fas fa-carrot agr-carrot-logo"></i>
                            </strong>
                        </a>
                        <a class="nav-link" href="tel:+375292848848" style="flex: 1"><i class="fas fa-phone-alt"></i> <b>(29) 2-848-848</b></a>
                        <a href="https://www.instagram.com/agrolavka.by" target="_blank" rel="noreferrer" class="me-2">
                            <img src="/assets/img/instagram.ico" alt="Instagram">
                        </a>
                        <a href="https://invite.viber.com/?g2=AQAg5Rkk2LluF0zHtRAvabtjZ4jDtGaaMApRoqe3%2FboHZogbep9nBgCTSKDPVqTl" target="_blank" rel="noreferrer">
                            <div class="agr-viber">
                                <i class="fab fa-viber"></i>
                            </div>
                        </a>
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
        </div>
    </nav>

    

</header>
