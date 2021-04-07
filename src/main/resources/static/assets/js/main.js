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
    let selectHeader = select('#header');
    let subheader = select('#subheader');
    let subheaderTop = select('#subheader-top');
    if (selectHeader) {
        const headerScrolled = () => {
            if (window.scrollY > 40) {
                selectHeader.classList.add('header-scrolled');
                subheader.classList.remove('show');
                subheaderTop.classList.remove('show');
            } else {
                selectHeader.classList.remove('header-scrolled');
                subheader.classList.add('show');
                subheaderTop.classList.add('show');
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
    
    /**
     * Scroll with ofset on page load with hash links in the url
     */
    window.addEventListener('load', () => {
        if (window.location.hash) {
            if (select(window.location.hash)) {
                scrollto(window.location.hash);
            }
        }
    });
    
    /** Agrolavka listeners. */
    document.querySelector('body').addEventListener('click', function(evt) {
        const addToCartBtn = evt.target.closest("[data-product-id][data-add]");
        if (addToCartBtn) {
            addToCartListener(evt, addToCartBtn);
        }
        const removeFromCartCartBtn = evt.target.closest("[data-product-id][data-remove]");
        if (removeFromCartCartBtn) {
            removeFromCartListener(evt, removeFromCartCartBtn);
        }
        const cartRemoveProductBtn = evt.target.closest("[data-product-id][data-remove-product-from-cart]");
        if (cartRemoveProductBtn) {
            cartRemoveProductListener(evt, cartRemoveProductBtn);
        }
        const cartQuantityPlusBtn = evt.target.closest("[data-product-quantity-plus]");
        if (cartQuantityPlusBtn) {
            const input = cartQuantityPlusBtn.closest(".input-group").querySelector("[data-product-quantity]");
            input.value = parseInt(input.value) + 1;
            cartProductQuantityChangeListener(evt, input);
        }
        const cartQuantityMinusBtn = evt.target.closest("[data-product-quantity-minus]");
        if (cartQuantityMinusBtn) {
            const input = cartQuantityMinusBtn.closest(".input-group").querySelector("[data-product-quantity]");
            input.value = parseInt(input.value) > 1 ? parseInt(input.value) - 1 : parseInt(input.value);
            cartProductQuantityChangeListener(evt, input);
        }
    }, true);
    
    document.querySelector('body').addEventListener('change', function(evt) {
        const cartProductQuantityInput = evt.target.closest("[data-product-id][data-product-quantity]");
        if (cartProductQuantityInput) {
            cartProductQuantityChangeListener(evt, cartProductQuantityInput);
        }
    }, true);
    
    var addToCartListener = function(evt, button) {
        evt.preventDefault();
        evt.stopPropagation();
        button.setAttribute('disabled', 'true');
        fetch('/api/agrolavka/public/cart/' + button.getAttribute('data-product-id'), {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            if (response.ok) {
                response.json().then(cart => {
                    button.removeAttribute('disabled');
                    button.removeAttribute('data-add');
                    button.setAttribute('data-remove', '');
                    _updateCartTotal(cart);
                    button.innerHTML = '<i class="fas fa-minus-circle me-2"></i> Из корзины';
                    button.classList.remove('btn-outline-success');
                    button.classList.add('btn-outline-danger');
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    };
    
    var removeFromCartListener = function(evt, button) {
        evt.preventDefault();
        evt.stopPropagation();
        button.setAttribute('disabled', 'true');
        fetch('/api/agrolavka/public/cart/' + button.getAttribute('data-product-id'), {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            if (response.ok) {
                response.json().then(cart => {
                    button.removeAttribute('disabled');
                    button.removeAttribute('data-remove');
                    button.setAttribute('data-add', '');
                    _updateCartTotal(cart);
                    button.innerHTML = '<i class="fas fa-cart-plus me-2"></i> В корзину';
                    button.classList.add('btn-outline-success');
                    button.classList.remove('btn-outline-danger');
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    };
    
    var cartRemoveProductListener = function (evt, button) {
        button.setAttribute('disabled', 'true');
        fetch('/api/agrolavka/public/cart/' + button.getAttribute('data-product-id'), {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            if (response.ok) {
                response.json().then(cart => {
                    button.closest(".card").remove();
                    
                    _updateCartTotal(cart);
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    };
    
    var cartProductQuantityChangeListener = function (evt, input) {
        const quantity = input.value;
        const productId = input.getAttribute('data-product-id');
        fetch('/api/agrolavka/public/cart/quantity/' + productId + '/' + quantity, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            if (response.ok) {
                response.json().then(cart => {
                    _updateCartTotal(cart);
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    };
    
    var _updateCartTotal = function (cart) {
        let total = 0;
        cart.positions.forEach(p => {
            total += p.price * p.quantity;
        });
        const totalLabel = document.querySelector("[data-total-price]");
        const cartTotalLabel = document.querySelector("[data-cart-price]");
        const totalStr = parseFloat(total).toFixed(2);
        const parts = totalStr.split(".");
        if (totalLabel) {
            totalLabel.innerHTML = parts[0] + '.<small>' + parts[1] + '</small> <small class="text-muted">BYN</small>';
        }
        if (cartTotalLabel) {
            cartTotalLabel.innerHTML = parts[0] + '.<small>' + parts[1] + '</small>'
        }
        const cartBadge = select('.agr-cart-badge');
        if (cartBadge) {
            cartBadge.innerHTML = cart.positions.length;
        }
        if (cart.positions.length === 0) {
            window.location.reload();
        }
    };
    
})();