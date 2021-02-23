/**
 * Template Name: Rapid - v4.0.1
 * Template URL: https://bootstrapmade.com/rapid-multipurpose-bootstrap-business-template/
 * Author: BootstrapMade.com
 * License: https://bootstrapmade.com/license/
 */
(function () {
    "use strict";

    /**
     * Easy selector helper function
     */
    const select = (el, all = false) => {
        el = el.trim()
        if (all) {
            return [...document.querySelectorAll(el)]
        } else if (el) {
            return document.querySelector(el);
    }
    }

    /**
     * Easy event listener function
     */
    const on = (type, el, listener, all = false) => {
        let selectEl = select(el, all)
        if (selectEl) {
            if (all) {
                selectEl.forEach(e => e.addEventListener(type, listener))
            } else {
                selectEl.addEventListener(type, listener)
            }
    }
    }

    /**
     * Easy on scroll event listener 
     */
    const onscroll = (el, listener) => {
        el.addEventListener('scroll', listener)
    }

    /**
     * Navbar links active state on scroll
     */
    let navbarlinks = select('#navbar .scrollto', true)
    const navbarlinksActive = () => {
        let position = window.scrollY + 200
        navbarlinks.forEach(navbarlink => {
            if (!navbarlink.hash)
                return
            let section = select(navbarlink.hash)
            if (!section)
                return
            if (position >= section.offsetTop && position <= (section.offsetTop + section.offsetHeight)) {
                navbarlink.classList.add('active')
            } else {
                navbarlink.classList.remove('active')
            }
        })
    }
    window.addEventListener('load', navbarlinksActive)
    onscroll(document, navbarlinksActive)

    /**
     * Scrolls to an element with header offset
     */
    const scrollto = (el) => {
        let header = select('#header')
        let offset = header.offsetHeight

        if (!header.classList.contains('header-scrolled')) {
            offset -= 20
        }

        let elementPos = select(el).offsetTop
        window.scrollTo({
            top: elementPos - offset,
            behavior: 'smooth'
        })
    }

    /**
     * Toggle .header-scrolled class to #header when page is scrolled
     */
    let selectHeader = select('#header')
    if (selectHeader) {
        const headerScrolled = () => {
            if (window.scrollY > 40) {
                selectHeader.classList.add('header-scrolled')
            } else {
                selectHeader.classList.remove('header-scrolled')
            }
        }
        window.addEventListener('load', headerScrolled)
        onscroll(document, headerScrolled)
    }

    /**
     * Back to top button
     */
    let backtotop = select('.back-to-top')
    if (backtotop) {
        const toggleBacktotop = () => {
            if (window.scrollY > 100) {
                backtotop.classList.add('active')
            } else {
                backtotop.classList.remove('active')
            }
        }
        window.addEventListener('load', toggleBacktotop)
        onscroll(document, toggleBacktotop)
    }

    /**
     * Mobile nav toggle
     */
    on('click', '.mobile-nav-toggle', function (e) {
        select('#navbar').classList.toggle('navbar-mobile')
        this.classList.toggle('bi-list')
        this.classList.toggle('bi-x')
    })

    /**
     * Mobile nav dropdowns activate
     */
    on('click', '.navbar .dropdown > a', function (e) {
        if (select('#navbar').classList.contains('navbar-mobile')) {
            e.preventDefault()
            this.nextElementSibling.classList.toggle('dropdown-active')
        }
    }, true)

    /**
     * Scrool with ofset on links with a class name .scrollto
     */
    on('click', '.scrollto', function (e) {
        if (select(this.hash)) {
            e.preventDefault()

            let navbar = select('#navbar')
            if (navbar.classList.contains('navbar-mobile')) {
                navbar.classList.remove('navbar-mobile')
                let navbarToggle = select('.mobile-nav-toggle')
                navbarToggle.classList.toggle('bi-list')
                navbarToggle.classList.toggle('bi-x')
            }
            scrollto(this.hash)
        }
    }, true)

    on('click', '#mobile-catalog-button', function (e) {
        select('.mobile-nav-toggle').click();
        setTimeout(() => {
            select('#mobile-catalog').click();
        }, 300);
    }, true);
    
    on('focus', '#products-search', function (e) {
        const searchResultOutput = select('#products-search-results-list');
        if (searchResultOutput.innerHTML) {
            searchResultOutput.classList.add("show");
            searchResultOutput.classList.add("list-group");
        }
    }, true);
    
    on('blur', '#products-search', function (e) {
        const searchResultOutput = select('#products-search-results-list');
        searchResultOutput.classList.remove("show");
        searchResultOutput.classList.remove("list-group");
    }, true);

    on('input', '#products-search', function (e) {
        const searchText = this.value;
        const searchResultOutput = select('#products-search-results-list');
        const noResult = '<li><a class="dropdown-item text-muted" href="#">По вашему запросу ничего не найдено</a></li>';
        if (searchText) {
            fetch(`/api/search?searchText=${searchText}`, {
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
                                sb += `<a href="/product/${product.id}/${product.name}" class="list-group-item list-group-item-action">
                                            <div class="d-flex w-100 justify-content-between">
                                                <h6 class="mb-1">${highlightText(product.name, searchText)}</h6>
                                                <small style="margin-left: 20px; min-width: 100px; text-align: right;" 
                                                    class="fw-bold">${parseFloat(product.price).toFixed(2)} BYN</small>
                                            </div>
                                            <div class="d-flex justify-content-between">
                                                <small class="text-muted">${product.group.name}</small>
                                            </div>
                                       </a>`;
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
            searchResultOutput.innerHTML = noResult;
        }
    }, true);
    
    function openQuickSearch() {
        const brand = select('#agrolavka-brand');
        const social = select('.social-links');
        const searchInputContainer = select('#products-search-container');
        const searchInput = select('#products-search');
        const searchResultOutput = select('#products-search-results-list');
        const isSearchOpen = brand.style.display === 'none';
        if (isSearchOpen) {
            brand.style.display = "";
            social.style.display = "";
            searchInputContainer.style.display = "none";
        } else {
            brand.style.display = "none";
            social.style.display = "none";
            searchInputContainer.style.display = "flex";
            searchResultOutput.classList.remove("show");
            searchResultOutput.classList.remove("list-group");
            searchResultOutput.innerHTML = '';
        }
        if (!isSearchOpen) {
            setTimeout(() => {
                scrollto('#products');
                searchInput.focus();
            }, 100);
        }
    }

    on('click', '#nav-search-button', function (e) {
        openQuickSearch();
    }, true);
    
    on('click', '#products-search-link', function (e) {
        openQuickSearch();
    }, true);

    on('click', '#products-search-close', function (e) {
        select('#nav-search-button').click();
    }, true);
    
    function highlightText(text, searchText) {
        const idx = text.toLowerCase().indexOf(searchText.toLowerCase());
        if (searchText.length > 0 && idx !== -1) {
            return `${text.substring(0, idx)}<span class="text-info" style="background-color: rgba(27,177,220,.07);">${text.substring(idx, idx + searchText.length)}</span>${text.substring(idx + searchText.length)}`;
        } else {
            return text;
        }
    }

    /**
     * Scroll with ofset on page load with hash links in the url
     */
    window.addEventListener('load', () => {
        if (window.location.hash) {
            if (select(window.location.hash)) {
                scrollto(window.location.hash)
            }
        }
    });

    /**
     * Porfolio isotope and filter
     */
    window.addEventListener('load', () => {
        let portfolioContainer = select('.portfolio-container');
        if (portfolioContainer) {
            let portfolioIsotope = new Isotope(portfolioContainer, {
                itemSelector: '.portfolio-item'
            });

            let portfolioFilters = select('#portfolio-flters li', true);

            on('click', '#portfolio-flters li', function (e) {
                e.preventDefault();
                portfolioFilters.forEach(function (el) {
                    el.classList.remove('filter-active');
                });
                this.classList.add('filter-active');

                portfolioIsotope.arrange({
                    filter: this.getAttribute('data-filter')
                });
                portfolioIsotope.on('arrangeComplete', function () {
                    AOS.refresh()
                });
            }, true);
        }

    });

    /**
     * Initiate portfolio lightbox 
     */
    const portfolioLightbox = GLightbox({
        selector: '.portfolio-lightbox'
    });

    /**
     * Portfolio details slider
     */
    new Swiper('.portfolio-details-slider', {
        speed: 400,
        loop: true,
        autoplay: {
            delay: 5000,
            disableOnInteraction: false
        },
        pagination: {
            el: '.swiper-pagination',
            type: 'bullets',
            clickable: true
        }
    });

    /**
     * Testimonials slider
     */
    new Swiper('.testimonials-slider', {
        speed: 600,
        loop: true,
        autoplay: {
            delay: 5000,
            disableOnInteraction: false
        },
        slidesPerView: 'auto',
        pagination: {
            el: '.swiper-pagination',
            type: 'bullets',
            clickable: true
        }
    });

    /**
     * Clients Slider
     */
    new Swiper('.clients-slider', {
        speed: 400,
        loop: true,
        autoplay: {
            delay: 5000,
            disableOnInteraction: false
        },
        slidesPerView: 'auto',
        pagination: {
            el: '.swiper-pagination',
            type: 'bullets',
            clickable: true
        },
        breakpoints: {
            320: {
                slidesPerView: 2,
                spaceBetween: 40
            },
            480: {
                slidesPerView: 3,
                spaceBetween: 60
            },
            640: {
                slidesPerView: 4,
                spaceBetween: 80
            },
            992: {
                slidesPerView: 6,
                spaceBetween: 120
            }
        }
    });

    /**
     * Animation on scroll
     */
    window.addEventListener('load', () => {
        AOS.init({
            duration: 1000,
            easing: 'ease-in-out',
            once: true,
            mirror: false
        })
    });

    var elements = document.getElementsByClassName("accordion-button-leaf");

    for (var i = 0; i < elements.length; i++) {
        elements[i].addEventListener('click', function () {
            var attribute = this.getAttribute("href");
            window.location.href = attribute;
        }, false);
    }
    
})()