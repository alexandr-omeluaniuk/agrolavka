<%-- 
    Document   : products-search
    Created on : Feb 23, 2021, 7:16:20 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="message"%>

<%-- any content can be specified here e.g.: --%>
<style>
    #products-search-container {
        margin-bottom: 0 !important;
        margin-right: 12px;
        margin-left: 12px;
    }
    #products-search-results-list {
        padding: 0;
        margin-top: 0px;
        max-height: calc(100vh - 100px);
        overflow-y: auto;
    }
    #products-search:focus {
        color: rgb(121,82,179);
        border-color: #1bb1dc;
        box-shadow: 0 0 0 3px rgb(121 82 179 / 25%);
        border-color: #7952b3;
    }
    #products-search {
        border-top-right-radius: .25rem;
        border-bottom-right-radius: .25rem;
    }
    #products-search-results-list h6 {
        max-width: 400px;
        white-space: pre-line;
        font-size: .9rem;
    }
    #products-search-results-list .text-muted {
        font-size: .8rem;
    }
    #products-search-results-list .highlighted-text {
        color: rgb(121,82,179);
        background-color: rgba(121,82,179,.07);
    }
</style>
<div class="input-group mb-3" id="products-search-container">
    <input type="search" class="form-control" aria-label="Поиск товаров" id="products-search" placeholder="Быстрый поиск"
           autocomplete="off">
    <ul class="dropdown-menu shadow-sm" aria-labelledby="products-search-container" id="products-search-results-list"></ul>
    <span class="input-group-text"><i class="fas fa-search"></i></span>
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
        
        document.querySelector('#products-search').addEventListener('focus', function (e) {
            const searchResultOutput = document.querySelector('#products-search-results-list');
            if (searchResultOutput.innerHTML) {
                searchResultOutput.classList.add("show");
                searchResultOutput.classList.add("list-group");
            }
        });

        document.querySelector('#products-search').addEventListener('blur', function (e) {
            setTimeout(() => {
                const searchResultOutput = document.querySelector('#products-search-results-list');
                searchResultOutput.classList.remove("show");
                searchResultOutput.classList.remove("list-group");
            }, 100);
        }, true);

        document.querySelector('#products-search').addEventListener('input', function (e) {
            const searchText = this.value;
            const searchResultOutput = document.querySelector('#products-search-results-list');
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
                                    sb += 
                                        '<li>'
                                            + '<a class="dropdown-item" href="/product/' + product.id + '?name='
                                                    + encodeURIComponent(product.name) + '">'
                                                + '<div class="d-flex w-100 justify-content-between">'
                                                    + '<h6 class="mb-1">' + highlightText(product.name, searchText) + '</h6>'
                                                    + '<small style="margin-left: 10px; min-width: 80px; text-align: right;"' 
                                                        + 'class="fw-bold">' + parseFloat(product.price).toFixed(2) + ' BYN</small>'
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