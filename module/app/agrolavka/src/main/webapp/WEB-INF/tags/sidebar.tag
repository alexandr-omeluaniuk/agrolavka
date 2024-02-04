<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<nav class="agr-menu-sidebar shadow-2-strong">
    <div class="agr-menu-container">
        <div class="d-flex align-items-center p-3" style="position: relative; z-index: 700;">
            <a href="/">
                <h3 class="mb-0"><i class="fas fa-carrot agr-carrot-logo"></i></h3>
            </a>
            <h4 class="text-center mb-0" style="flex: 1">Агролавка</h4>
            <x-agr-menu-item-icon icon="times" color="light" class="agr-mobile-menu-close-btn"></x-agr-menu-item-icon>
        </div>
        <div class="agr-mobile-menu-slider">
            <div class="agr-mobile-menu-slide active agr-mobile-menu-main-slide">
                <div class="list-group list-group-flush py-2" style="overflow-y: auto;">
                    <x-agr-menu-item class="p-1" link="/" label="Главная" icon="home" icon-color="light" label-color="dark"></x-agr-menu-item>
                    <x-agr-menu-item class="p-1" link="/catalog" label="Каталог продукции" icon="sitemap" icon-color="primary" label-color="primary" 
                                     end-icon="chevron-right" link-attributes="data-catalog='-1'" bold-label></x-agr-menu-item>
                    <x-agr-menu-item class="p-1" link="/promotions" label="Акции" icon="fire" icon-color="danger" label-color="danger" bold-label></x-agr-menu-item>
                    <x-agr-menu-item class="p-1" link="/shops" label="Магазины" icon="store" icon-color="light" label-color="dark"></x-agr-menu-item>
                    <x-agr-menu-item class="p-1" link="/delivery" label="Доставка" icon="truck" icon-color="light" label-color="dark"></x-agr-menu-item>
                    <x-agr-menu-item class="p-1" link="/discount" label="Дисконтная программа" icon="percent" icon-color="light" label-color="dark"></x-agr-menu-item>
                    <x-agr-menu-item class="p-1" link="#contacts" label="Контакты" icon="map-marker-alt" icon-color="light" label-color="dark"></x-agr-menu-item>
                    <!--x-agr-menu-item class="p-1" link="/feedback" label="Написать нам" icon="comment" icon-color="light" label-color="dark"></x-agr-menu-item-->
                    <t:menu-item-link-external link="https://www.instagram.com/agrolavka.by" label="Инстаграм" type="Instagram"></t:menu-item-link-external>
                    <t:menu-item-link-external link="https://invite.viber.com/?g2=AQAg5Rkk2LluF0zHtRAvabtjZ4jDtGaaMApRoqe3%2FboHZogbep9nBgCTSKDPVqTl" label="Вайбер" type="Viber"></t:menu-item-link-external>
                </div>
            </div>
        </div>
        <div class="d-flex w-100 align-items-center justify-content-center">
            <div class="text-center p-3">
                <x-agr-menu-item-icon icon="trademark" color="light"></x-agr-menu-item-icon>
                <small>Торговая марка</small>, <small class="text-muted">Все права защищены</small>
            </div>
        </div>
    </div>
</nav>

<div class="agr-backdrop"></div>
