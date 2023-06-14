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
                    <x-agr-menu-item class="p-1" link="/feedback" label="Написать нам" icon="comment" icon-color="light" label-color="dark"></x-agr-menu-item>
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
<script type="text/javascript">
    (function () {
        "use strict";
        let catalog = {};
        fetch('/api/agrolavka/public/catalog', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.ok) {
                response.json().then(json => catalog = json);
            }
        });

        const findGroup = (id) => {
            const keys = Object.keys(catalog);
            for (let i = 0; i < keys.length; i++) {
                const groups = catalog[keys[i]].filter(group => group.externalId === id);
                if (groups.length > 0)
                    return groups[0];
            }
        };

        const openCatalog = (id) => {
            const data = catalog[id].sort((a, b) => {
                if (a.topCategory && !b.topCategory) {
                    return -1;
                } else if (!a.topCategory && b.topCategory) {
                    return 1;
                } else {
                    if (a.name > b.name) {
                        return 1;
                    } else if (a.name < b.name) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            let sb = '';
            data.forEach(item => {
                const isLeaf = !catalog[item.externalId];
                const image = item.images[0];
                const imageUrl = image ? '/media/' + image.fileNameOnDisk + '?timestamp=' + image.lastModifiedDate : '/assets/img/no-image.png';
                sb += '<x-agr-catalog-menu-item class="p-1" data-label="' + item.name + '" ' 
                        + (isLeaf ? 'data-href="' + buildProguctGroupUrl(item) + '" data-catalog-nav-link=""' : ' data-catalog="' + item.externalId + '"') + (item.topCategory ? ' data-top-category' : '') 
                        + ' data-image="' + imageUrl + '"></x-agr-catalog-menu-item>';
            });
            const category = findGroup(id);
            const backButton = '<a class="agr-mobile-menu-back-button" data-catalog-back="' + id + '">'
                    + '<div class="list-group-item list-group-item-action d-flex rounded-pill p-2 align-items-center">'
                    + '<i class="fas fa-chevron-left fa-fw me-1"></i>'
                    + '<span class="ms-2">' + (id === "-1" ? "Каталог продукции" : category.name) + '</span>'
                    + '</div>'
                    + '</a>';
            const template = '<div class="agr-mobile-menu-slide" data-slide="' + id + '">'
                    + '<div class="ps-2 pe-2 list-group list-group-flush">'
                    + backButton
                    + '<a class="agr-link agr-external-link text-center mt-2 mb-1" href="' + (category ? buildProguctGroupUrl(category) : '/catalog') + '" style="text-decoration: underline;">'
                    + '<small>«Посмотреть все»</small></a>'
                    + '</div>'
                    + '<div class="list-group list-group-flush mt-2 agr-scrollable-list-group">' + sb + '</div>'
                    + '</div>';
            const element = createElementFromHTML(template);
            const catalogContainer = document.querySelector('.agr-mobile-menu-slider');
            catalogContainer.appendChild(element);
            const activeSlide = catalogContainer.querySelector('.active');
            activeSlide.classList.remove('active');
            activeSlide.classList.add('left');
            element.classList.add('active');
        };

        const closeCatalog = (id) => {
            const catalogContainer = document.querySelector('.agr-mobile-menu-slider');
            const activeSlide = catalogContainer.lastChild;
            const prevSlide = catalogContainer.children[catalogContainer.children.length - 2];
            prevSlide.classList.remove('left');
            prevSlide.classList.add('right');
            prevSlide.classList.add('active');
            setTimeout(() => {
                activeSlide.remove();
                prevSlide.classList.remove('right');
            }, 310);
        };

        const createElementFromHTML = (htmlString) => {
            var div = document.createElement('div');
            div.innerHTML = htmlString.trim();
            return div.firstElementChild;
        };

        const buildProguctGroupUrl = (group) => {
            let sb = '/catalog';
            const parts = [];
            let current = group;
            while (current) {
                parts.push(current.url);
                current = findGroup(current.parentId);
            }
            parts.reverse().forEach(url => sb += '/' + url);
            return sb;
        };

        document.querySelector('.agr-mobile-menu-slider').addEventListener('click', (evt) => {
            const catalogLink = evt.target.closest('a[data-catalog]');
            if (catalogLink) {
                evt.stopPropagation();
                evt.preventDefault();
                openCatalog(catalogLink.getAttribute('data-catalog'));
                return;
            }
            const catalogBackLink = evt.target.closest('a[data-catalog-back]');
            if (catalogBackLink) {
                evt.stopPropagation();
                evt.preventDefault();
                closeCatalog(catalogBackLink.getAttribute('data-catalog-back'));
                return;
            }
            const link = evt.target.closest('a[data-catalog-nav-link]');
            if (link) {
                evt.stopPropagation();
                window.location.href = link.getAttribute('href');
                return;
            }
            const externalLink = evt.target.closest('.agr-external-link');
            if (externalLink) {
                return;
            }
            evt.stopPropagation();
            evt.preventDefault();
        });
    })();
</script>