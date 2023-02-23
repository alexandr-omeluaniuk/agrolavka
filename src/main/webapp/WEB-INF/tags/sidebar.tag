<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<nav class="agr-menu-sidebar shadow-2-strong">
    <div class="agr-menu-container">
        <div class="d-flex align-items-center shadow-2 p-3" style="position: relative; z-index: 700;">
            <a href="/">
                <h3 class="mb-0"><i class="fas fa-carrot agr-carrot-logo"></i></h3>
            </a>
            <h4 class="text-center mb-0" style="flex: 1">Агролавка</h4>
            <button class="navbar-toggler agr-mobile-menu-close-btn" type="button">
                <i class="fas fa-times fa-fw"></i>
            </button>
        </div>
        <div class="agr-mobile-menu-slider">
            <div class="agr-mobile-menu-slide active">
                <div class="list-group list-group-flush mt-4 ">
                    <a href="/" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-home fa-fw me-3"></i><span>Главная</span>
                    </a>
                    <a href="/catalog" class="list-group-item list-group-item-action py-2 ripple d-flex align-items-center" aria-current="true" data-catalog="-1">
                        <div style="flex: 1;"><i class="fas fa-box-open fa-fw me-3"></i><b class="agr-menu-item-higlight">Каталог продукции</b></div>
                        <i class="fas fa-chevron-right"></i>
                    </a>
                    <a href="/promotions" class="list-group-item list-group-item-action py-2 ripple text-danger agr-mobile-menu-link" aria-current="true">
                        <i class="fas fas fa-fire fa-fw me-3"></i><b>Акции</b>
                    </a>
                    <a href="/shops" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-store fa-fw me-3"></i><span>Магазины</span>
                    </a>
                    <a href="/delivery" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-truck fa-fw me-3"></i><span>Доставка</span>
                    </a>
                    <a href="/discount" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-percent fa-fw me-3"></i><span>Дисконтная программа</span>
                    </a>
                    <a href="#contacts" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-map-marker-alt fa-fw me-3"></i><span>Контакты</span>
                    </a>
                    <a href="/feedback" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-comment fa-fw me-3"></i><span>Написать нам</span>
                    </a>
                    <a class="list-group-item list-group-item-action py-2 ripple d-flex align-items-center agr-external-link" href="https://www.instagram.com/agrolavka.by" target="_blank" rel="noreferrer">
                        <img src="/assets/img/instagram.ico" alt="Instagram"><span class="ms-2">Инстаграм</span>
                    </a>
                    <a class="list-group-item list-group-item-action py-2 ripple d-flex align-items-center agr-external-link" href="https://invite.viber.com/?g2=AQAg5Rkk2LluF0zHtRAvabtjZ4jDtGaaMApRoqe3%2FboHZogbep9nBgCTSKDPVqTl" target="_blank" rel="noreferrer">
                        <i class="fab fa-viber" style="font-size: 32px; color: #574e92;"></i><span class="ms-2">Вайбер</span>
                    </a>
                </div>
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
                const attributes = isLeaf ? ' href="' + buildProguctGroupUrl(item) + '" data-catalog-nav-link=""' : ' data-catalog="' + item.externalId + '"';
                const nextIcon = isLeaf ? '' : '<i class="fas fa-chevron-right fa-fw"></i>';
                sb += '<a class="agr-menu-catalog-group-link list-group-item list-group-item-action py-2 d-flex align-items-center '
                        + (item.topCategory ? 'agr-menu-item-top-category' : '') + '" ' + attributes + '>'
                        + '<span class="ms-2" style="flex: 1">' + item.name + '</span>'
                        + nextIcon
                        + '</a>';
            });
            const category = findGroup(id);
            const backButton = '<a class="list-group-item list-group-item-action py-2 agr-mobile-menu-back-button d-flex align-items-center" data-catalog-back="' + id + '">'
                    + '<i class="fas fa-chevron-left fa-fw me-1"></i>'
                    + '<span class="ms-2">' + (id === "-1" ? "Каталог продукции" : category.name) + '</span>'
                    + '</a>';
            const template = '<div class="agr-mobile-menu-slide" data-slide="' + id + '">'
                    + '<div class="list-group list-group-flush mt-2">'
                    + backButton
                    + '<a class="agr-link agr-external-link text-center mt-2" href="' + (category ? buildProguctGroupUrl(category) : '/catalog') + '" style="text-decoration: underline;">'
                    + '«Посмотреть все»</a>'
                    + '</div>'
                    + '<div class="list-group list-group-flush mt-2 pb-2" style="overflow-y: auto;">' + sb + '</div>'
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