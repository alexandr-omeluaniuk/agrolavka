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
                <div class="arg-top-navbar">
                    <div class="d-flex align-items-center mb-2">
                        <a href="/" class="navbar-brand nav-link" style="border-right: 1px solid #ffffff70; margin-right: 0;">
                            <strong class="d-flex justify-content-center align-items-center">
                                <i class="fas fa-carrot agr-carrot-logo"></i>
                            </strong>
                        </a>
                        <a class="nav-link" href="tel:+375292848848" style="flex: 1"><i class="fas fa-phone-alt"></i> +375 29 2-848-848 (МТС)</a>
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

    <nav class="agr-menu-sidebar shadow-2-strong">
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
                <div class="list-group list-group-flush mx-3 mt-4 ">
                    <a href="/" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-home fa-fw me-3"></i><span>Главная</span>
                    </a>
                    <a href="/catalog" class="list-group-item list-group-item-action py-2 ripple d-flex align-items-center" aria-current="true" data-catalog="-1">
                        <div style="flex: 1;"><i class="fas fa-box-open fa-fw me-3"></i><span>Каталог продукции</span></div>
                        <i class="fas fa-chevron-right"></i>
                    </a>
                    <a href="/shops" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-store fa-fw me-3"></i><span>Магазины</span>
                    </a>
                    <a href="/promotions" class="list-group-item list-group-item-action py-2 ripple text-danger agr-mobile-menu-link" aria-current="true">
                        <i class="fas fas fa-fire fa-fw me-3"></i><span>Акции</span>
                    </a>
                    <a href="/delivery" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-truck fa-fw me-3"></i><span>Доставка</span>
                    </a>
                    <a href="/discount" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-percent fa-fw me-3"></i><span>Дисконтная программа</span>
                    </a>
                    <a href="#contacts" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-separator agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-map-marker-alt fa-fw me-3"></i><span>Контакты</span>
                    </a>
                    <a href="/feedback" class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-link" aria-current="true">
                        <i class="fas fa-comment fa-fw me-3"></i><span>Написать нам</span>
                    </a>
                    <a class="list-group-item list-group-item-action py-2 ripple d-flex align-items-center agr-mobile-menu-separator" href="https://www.instagram.com/agrolavka.by" target="_blank" rel="noreferrer">
                        <img src="/assets/img/instagram.ico" alt="Instagram"><span class="ms-2 d-block d-lg-none">Инстаграм</span>
                    </a>
                    <a class="list-group-item list-group-item-action py-2 ripple d-flex align-items-center" href="https://invite.viber.com/?g2=AQAg5Rkk2LluF0zHtRAvabtjZ4jDtGaaMApRoqe3%2FboHZogbep9nBgCTSKDPVqTl" target="_blank" rel="noreferrer">
                        <i class="fab fa-viber" style="font-size: 32px; color: #574e92;"></i><span class="ms-2 d-block d-lg-none">Вайбер</span>
                    </a>
                </div>
            </div>
        </div>
    </nav>

</header>
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
                if (a.name > b.name) {
                    return 1;
                } else if (a.name < b.name) {
                    return -1;
                } else {
                    return 0;
                }
            });
            let sb = '';
            data.forEach(item => {
                const isLeaf = !catalog[item.externalId];
                const attributes = isLeaf ? ' href="' + buildProguctGroupUrl(item) + '" data-catalog-nav-link=""' : ' data-catalog="' + item.externalId + '"';
                const nextIcon = isLeaf ? '' : '<i class="fas fa-chevron-right fa-fw"></i>';
                sb += '<a class="list-group-item list-group-item-action py-2 ripple d-flex align-items-center" ' + attributes + '>'
                        + '<span class="ms-2" style="flex: 1">' + item.name + '</span>'
                        + nextIcon
                        + '</a>';
            });
            const backButton = '<a class="list-group-item list-group-item-action py-2 ripple agr-mobile-menu-back-button d-flex align-items-center" data-catalog-back="' + id + '">'
                    + '<i class="fas fa-chevron-left fa-fw me-1"></i>'
                    + '<span class="ms-2">' + (id === "-1" ? "Каталог продукции" : findGroup(id).name) + '</span>'
                    + '</a>';
            const template = '<div class="agr-mobile-menu-slide" data-slide="' + id + '">'
                    + '<div class="list-group list-group-flush mx-3 mt-2">' + backButton + '</div>'
                    + '<div class="list-group list-group-flush mx-3 mt-4 pb-2" style="overflow-y: auto;">' + sb + '</div>'
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
            evt.stopPropagation();
            evt.preventDefault();
        });
    })();
</script>

