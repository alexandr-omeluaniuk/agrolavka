<%-- 
    Document   : quick-search-mobile
    Created on : Feb 23, 2021, 7:16:20 PM
    Author     : alex
--%>

<%@tag description="Quick search mobile" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<div class="input-group me-2" id="agr-quick-search-container-mobile-trigger">
    <input type="search" class="form-control" aria-label="Поиск товаров" id="agr-quick-search-input-mobile-trigger" placeholder="Быстрый поиск"
           autocomplete="off">
    <span class="input-group-text">
        <i class="fas fa-search" style="color:white;" data-mdb-toggle="modal" data-mdb-target="#agr-quick-search-mobile-modal"></i>
    </span>
</div>
<div class="modal fade" tabindex="-1" id="agr-quick-search-mobile-modal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <input type="search" class="form-control me-2 modal-title" aria-label="Поиск товаров" id="agr-quick-search-input-mobile"
                       placeholder="Быстрый поиск" autocomplete="off" tabindex="1">
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <ul class="list-group" id="agr-quick-search-result-mobile"></ul>
            </div>
        </div>
    </div>
</div>

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

        document.querySelector('#agr-quick-search-input-mobile-trigger').addEventListener('focus', function (e) {
            const modalToggler = document.querySelector('[data-mdb-target="#agr-quick-search-mobile-modal"]');
            modalToggler.click();
            setTimeout(() => {
                document.querySelector('#agr-quick-search-input-mobile').focus();
            }, 700);
        });

        document.querySelector('#agr-quick-search-input-mobile').addEventListener('input', function (e) {
            const searchText = this.value;
            const searchResultOutput = document.querySelector('#agr-quick-search-result-mobile');
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