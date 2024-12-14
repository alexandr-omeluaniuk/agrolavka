(function () {
    "use strict";

    const clearTextElement = document.querySelector('#agr-quick-search-input-mobile-clear');
    const searchInput = document.querySelector('#agr-quick-search-input-mobile');

    let controller;

    function highlightText(text, searchText) {
        const idx = text.toLowerCase().indexOf(searchText.toLowerCase());
        if (searchText.length > 0 && idx !== -1) {
            return text.substring(0, idx) + '<span class="highlighted-text">' +
                    text.substring(idx, idx + searchText.length) + '</span>' + text.substring(idx + searchText.length);
        } else {
            return text;
        }
    }
    clearTextElement.addEventListener('click', function (e) {
        searchInput.value = '';
        searchInput.focus();
        searchInput.dispatchEvent(new Event('input', { bubbles: true }));
    });

    document.querySelector('#agr-quick-search-input-mobile-trigger').addEventListener('focus', function (e) {
        const modalToggler = document.querySelector('[data-mdb-target="#agr-quick-search-mobile-modal"]');
        modalToggler.click();
        setTimeout(() => {
            document.querySelector('#agr-quick-search-input-mobile').focus();
        }, 700);
    });
    const input = document.querySelector('#agr-quick-search-input-mobile-trigger');
    searchInput.addEventListener('input', function (e) {
        const searchText = this.value;
        const searchResultOutput = document.querySelector('#agr-quick-search-result-mobile');
        const noResult = '<li><a class="dropdown-item text-muted" href="#">По вашему запросу ничего не найдено</a></li>';
        if (searchText) {
            clearTextElement.classList.remove("d-none");
            if (controller) {
                controller.abort();
            }
            controller = new AbortController();
            fetch('/api/agrolavka/public/search?searchText=' + encodeURIComponent(searchText), {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                signal: controller.signal
            }).then(function (response) {
                if (response.ok) {
                    response.json().then(json => {
                        let sb = '';
                        const data = json.data;
                        const count = json.count;
                        const searchTerm = json.searchText;
                        if (data.length === 0) {
                            sb = noResult;
                        } else {
                            data.forEach(product => {
                                const price = parseFloat(product.price).toFixed(2);
                                const priceRub = price.split('.')[0];
                                const priceCent = price.split('.')[1];
                                sb +=
                                        '<li class="agr-product-search-link">'
                                        + '<a class="dropdown-item" href="' + product.url + '">'
                                        + '<div class="d-flex w-100 justify-content-between">'
                                        + '<h6 class="mb-1">' + highlightText(product.name, searchTerm) + '</h6>'
                                        + '<small style="margin-left: 10px; min-width: 80px; text-align: right;"'
                                        + 'class="fw-bold">' + priceRub
                                        + '.<span style="font-size: .9em; margin-right: 5px;">' + priceCent + '</span>'
                                        + '<span class="text-muted">BYN</span>'
                                        + '</small>'
                                        + '</div>'
                                        + '<div class="d-flex justify-content-between">'
                                            + '<small class="text-muted">' + (product.group ? product.group.name : '') + '</small>'
                                            + '<div>' + (product.discount ? '<span class="badge bg-danger me-1"><b>-' + product.discount.discount + '%</b></span>' : '')
                                            + '<span class="agr-quantity badge ' + (product.quantity ? 'bg-success' : 'bg-gray') + '">'
                                                    + (product.quantity ? 'в наличии' : 'под заказ') + '</span></div>'
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
            clearTextElement.classList.add("d-none");
            searchResultOutput.innerHTML = '';
        }
    });

    function randomInterval(min, max) {
        return Math.floor(Math.random() * (max - min + 1) + min)
    }

    var textCursor = 0;
    const texts = [
        "Мульчирующая пленка",
        "Aqualis Еврохим",
        "Капельная лента",
        "Капельный полив",
        "Балерина гербицид"
    ]
    const text = texts[randomInterval(0, texts.length - 1)];
    function animateText() {
        if (textCursor <= text.length) {
            input.placeholder = text.substring(0, textCursor) + "";
            textCursor++;
            setTimeout(animateText, randomInterval(50, 150));
        } else {
            animateTextReverse();
        }
    }
    function animateTextReverse() {
        if (textCursor > 0) {
            input.placeholder = text.substring(0, textCursor);
            textCursor--;
            setTimeout(animateTextReverse, randomInterval(10, 40));
        } else {
            input.placeholder = 'Быстрый поиск';
        }
    }
    animateText();
})();


(function () {
    "use strict";

    let controller;

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
        container.style.width = (window.innerWidth - 650) + 'px';
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
            if (controller) {
                controller.abort();
            }
            controller = new AbortController();
            fetch('/api/agrolavka/public/search?searchText=' + encodeURIComponent(searchText), {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                signal: controller.signal
            }).then(function (response) {
                if (response.ok) {
                    response.json().then(json => {
                        let sb = '';
                        const data = json.data;
                        const count = json.count;
                        const searchTerm = json.searchText;
                        if (data.length === 0) {
                            sb = noResult;
                        } else {
                            data.forEach(product => {
                                const price = parseFloat(product.price).toFixed(2);
                                const priceRub = price.split('.')[0];
                                const priceCent = price.split('.')[1];
                                let priceMaxRub, priceMaxCent;
                                if (product.maxPrice) {
                                    const priceMax = parseFloat(product.maxPrice).toFixed(2);
                                    priceMaxRub = priceMax.split('.')[0];
                                    priceMaxCent = priceMax.split('.')[1];
                                }
                                sb += 
                                    '<li class="agr-product-search-link">'
                                        + '<a class="dropdown-item" href="' + product.url + '">'
                                            + '<div class="d-flex w-100 justify-content-between">'
                                                + '<h6 class="mb-1">' + highlightText(product.name, searchTerm) + '</h6>'
                                                + '<small style="margin-left: 10px; min-width: 80px; text-align: right;" class="fw-bold">' + priceRub
                                                        + '.<span style="font-size: .9em;">' + priceCent + '</span>'
                                                        + (priceMaxRub ? ' - ' + priceMaxRub + '.<span style="font-size: .9em;">' + priceMaxCent + '</span>' : '')
                                                    + '<span class="text-muted ms-1">BYN</span>'
                                                + '</small>'
                                            + '</div>'
                                            + '<div class="d-flex justify-content-between">'
                                                + '<small class="text-muted">' + (product.group ? product.group.name : '') + '</small>'
                                                + '<div>' + (product.discount ? '<span class="badge bg-danger me-1"><b>-' + product.discount.discount + '%</b></span>' : '')
                                                    + '<span class="agr-quantity badge ' + (product.quantity ? 'bg-success' : 'bg-gray') + '">'
                                                    + (product.quantity ? 'в наличии' : 'под заказ') + '</span></div>'
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

    function randomInterval(min, max) {
        return Math.floor(Math.random() * (max - min + 1) + min)
    }

    var textCursor = 0;
    const texts = [
        "Мульчирующая пленка",
        "Aqualis Еврохим",
        "Капельная лента",
        "Капельный полив",
        "Балерина гербицид"
    ]
    const text = texts[randomInterval(0, texts.length - 1)];
    function animateText() {
        if (textCursor <= text.length) {
            input.placeholder = text.substring(0, textCursor);
            textCursor++;
            setTimeout(animateText, randomInterval(50, 150));
        } else {
            animateTextReverse();
        }
    }
    function animateTextReverse() {
        if (textCursor > 0) {
            input.placeholder = text.substring(0, textCursor);
            textCursor--;
            setTimeout(animateTextReverse, randomInterval(10, 40));
        } else {
            input.placeholder = 'Быстрый поиск';
        }
    }
    animateText();
})();
