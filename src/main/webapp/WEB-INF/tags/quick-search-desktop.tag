<%-- 
    Document   : products-search
    Created on : Feb 23, 2021, 7:16:20 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<div class="input-group me-4 d-lg-none d-xl-flex" id="agr-quick-search-container-desktop">
    <input type="search" class="form-control" aria-label="Поиск товаров" id="agr-quick-search-input-desktop" placeholder="Быстрый поиск"
           autocomplete="off">
    <ul class="dropdown-menu" aria-labelledby="agr-quick-search-container-desktop" id="agr-quick-search-result-desktop"></ul>
    <span class="input-group-text"><i class="fas fa-search" style="color:white;"></i></span>
</div>

<button type="button" class="btn btn-outline-light d-none d-lg-block d-xl-none btn-floating me-4" id="agr-quick-search-switcher-desktop">
    <i class="fas fa-search"></i>
</button>

<script>
    (function () {
        "use strict";
        
        function highlightText(text, searchText) {
            const idx = text.toLowerCase().indexOf(searchText.toLowerCase());
            if (searchText.length > 0 && idx !== -1) {
                return text.substring(0, idx) + '<span class="highlighted-text">' +
                    text.substring(idx, idx + searchText.length) + '</span>' + text.substring(idx + searchText.length);
            } else {
                return text;
            }
        }
        const input = document.querySelector('#agr-quick-search-input-desktop');
        const switcher =  document.querySelector('#agr-quick-search-switcher-desktop');
        switcher.addEventListener('click', function (e) {
            const container = document.querySelector('#agr-quick-search-container-desktop');
            switcher.classList.remove('d-lg-block');
            container.classList.remove('d-lg-none');
            input.focus();
        });
        
        input.addEventListener('focus', function (e) {
            const container = document.querySelector('#agr-quick-search-container-desktop');
            const navLinks = document.querySelector('.navbar-nav');
            navLinks.style.display = 'none';
            container.style.width = (window.innerWidth - 570) + 'px';
            const searchResultOutput = document.querySelector('#agr-quick-search-result-desktop');
            if (searchResultOutput.innerHTML) {
                searchResultOutput.classList.add("show");
                searchResultOutput.classList.add("list-group");
            }
        });

        document.querySelector('#agr-quick-search-container-desktop').addEventListener('blur', function (e) {
            if (e.relatedTarget === null) {
                const container = document.querySelector('#agr-quick-search-container-desktop');
                container.style.width = null;
                const searchResultOutput = document.querySelector('#agr-quick-search-result-desktop');
                searchResultOutput.classList.remove("show");
                searchResultOutput.classList.remove("list-group");
                setTimeout(() => {
                    switcher.classList.add('d-lg-block');
                    container.classList.add('d-lg-none');
                    const navLinks = document.querySelector('.navbar-nav');
                    navLinks.style.display = null;
                }, 300);
            }
        }, true);

        document.querySelector('#agr-quick-search-input-desktop').addEventListener('input', function (e) {
            const searchText = this.value;
            const searchResultOutput = document.querySelector('#agr-quick-search-result-desktop');
            const noResult = '<li><a class="dropdown-item text-muted" href="#">По вашему запросу ничего не найдено</a></li>';
            if (searchText) {
                fetch('/api/agrolavka/public/search?searchText=' + searchText, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }
                }).then(function (response) {
                    if (response.ok) {
                        response.json().then(json => {
                            let sb = '';
                            const data = json.data;
                            const count = json.count;
                            if (data.length === 0) {
                                sb = noResult;
                            } else {
                                data.forEach(product => {
                                    const price = parseFloat(product.discountPrice ? product.discountPrice : product.price).toFixed(2);
                                    const priceRub = price.split('.')[0];
                                    const priceCent = price.split('.')[1];
                                    sb += 
                                        '<li class="agr-product-search-link">'
                                            + '<a class="dropdown-item" href="' + product.url + '">'
                                                + '<div class="d-flex w-100 justify-content-between">'
                                                    + '<h6 class="mb-1">' + highlightText(product.name, searchText) + '</h6>'
                                                    + '<small style="margin-left: 10px; min-width: 80px; text-align: right;"' 
                                                        + 'class="fw-bold">' + priceRub
                                                        + '.<span style="font-size: .9em; margin-right: 5px;">' + priceCent + '</span>'
                                                        + '<span class="text-muted">BYN</span>'
                                                    + '</small>'
                                                + '</div>'
                                                + '<div class="d-flex justify-content-between">'
                                                    + '<small class="text-muted">' + (product.group ? product.group.name : '') + '</small>'
                                                    + '<span class="agr-quantity badge ' + (product.quantity ? 'bg-success' : 'bg-danger') + '">' 
                                                        + (product.quantity ? 'в наличии' : 'под заказ') + '</span>'
                                                + '</div>'
                                            + '</a>'
                                        + '</li>';
                                });
                            }
                            searchResultOutput.innerHTML = sb;
                            searchResultOutput.classList.add("show");
                            searchResultOutput.classList.add("list-group");
                        });
                    }
                }).catch(error => {
                    console.error('HTTP error occurred: ' + error);
                });
            } else {
                searchResultOutput.innerHTML = '';
            }
        });
        
    })();
</script>