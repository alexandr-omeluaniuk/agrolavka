/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global parseFloat, fetch */

(function () {
    "use strict";

    let previuosScrollPosition = null;
    document.addEventListener('scroll', function () {
        const topNavbar = document.querySelector('.arg-top-navbar');
        let currentScrollDirection = null;
        if (previuosScrollPosition !== null) {
            if (previuosScrollPosition > window.scrollY) {
                currentScrollDirection = 'UP';
            } else if (previuosScrollPosition < window.scrollY) {
                currentScrollDirection = 'DOWN';
            }
        }
        if (window.scrollY < 80 || currentScrollDirection === 'UP') {
            topNavbar.style.display = 'block';
            topNavbar.style.opacity = 1;
            topNavbar.style.height = '41px';
        } else if (currentScrollDirection === 'DOWN') {
            topNavbar.style.opacity = 0;
            topNavbar.style.height = 0;
            setTimeout(() => {
                if (topNavbar.style.height === 0 || topNavbar.style.height === '0px') {
                    topNavbar.style.display = 'none';
                }
            }, 300);
        }
        previuosScrollPosition = window.scrollY;
    });
    
    document.querySelector('body').addEventListener('click', function(evt) {
        const addToCartBtn = evt.target.closest("[data-product-id][data-add]");
        if (addToCartBtn) {
            addToCartListener(evt, addToCartBtn);
        }
        const removeFromCartCartBtn = evt.target.closest("[data-product-id][data-remove]");
        if (removeFromCartCartBtn) {
            removeFromCartListener(evt, removeFromCartCartBtn);
        }
        const orderOneClickButton = evt.target.closest("[data-product-id][data-order]");
        if (orderOneClickButton) {
            orderOneClickButtonListener(evt, orderOneClickButton);
        }
        const cartRemoveProductBtn = evt.target.closest("[data-product-id][data-remove-product-from-cart]");
        if (cartRemoveProductBtn) {
            cartRemoveProductListener(evt, cartRemoveProductBtn);
        }
        const cartQuantityPlusBtn = evt.target.closest("[data-product-quantity-plus]");
        if (cartQuantityPlusBtn) {
            const input = cartQuantityPlusBtn.closest("div").querySelector("[data-product-quantity]");
            input.value = parseInt(input.value) + 1;
            cartProductQuantityChangeListener(evt, input);
        }
        const cartQuantityMinusBtn = evt.target.closest("[data-product-quantity-minus]");
        if (cartQuantityMinusBtn) {
            const input = cartQuantityMinusBtn.closest("div").querySelector("[data-product-quantity]");
            input.value = parseInt(input.value) > 1 ? parseInt(input.value) - 1 : parseInt(input.value);
            cartProductQuantityChangeListener(evt, input);
        }
        const productPhoto = evt.target.closest(".agr-product-photos");
        if (productPhoto) {
            selectProductPhotoListener(evt, productPhoto);
        }
    }, true);
    
    document.querySelector('body').addEventListener('change', function(evt) {
        const cartProductQuantityInput = evt.target.closest("[data-product-id][data-product-quantity]");
        if (cartProductQuantityInput) {
            cartProductQuantityChangeListener(evt, cartProductQuantityInput);
        }
    }, true);
    
    const navBar = document.querySelector('#agr-navbar');
    document.querySelectorAll('.nav-link', navBar).forEach(navLink => {
        navLink.addEventListener('click', function () {
            if (navBar.classList.contains('show')) {
                document.querySelector('button[data-mdb-target="#agr-navbar"]').click();
            }
        });
    });
    
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
    
    var orderOneClickButtonListener = function(evt, button) {
        evt.preventDefault();
        evt.stopPropagation();
        const productId = button.getAttribute('data-product-id');
        console.log(productId);
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
        const totalLabels = document.querySelectorAll("[data-total-price]");
        const cartTotalLabels = document.querySelectorAll("[data-cart-price]");
        const totalStr = parseFloat(total).toFixed(2);
        const parts = totalStr.split(".");
        totalLabels.forEach(el => {
            el.innerHTML = parts[0] + '.<small>' + parts[1] + '</small> <small class="text-muted">BYN</small>';
        });
        cartTotalLabels.forEach(el => {
            el.innerHTML = parts[0] + '.<small>' + parts[1] + '</small>';
        });
        const cartBadges = document.querySelectorAll('.agr-cart-badge');
        cartBadges.forEach(el => {
            el.innerHTML = cart.positions.length;
        });
        if (cart.positions.length === 0 && window.location.pathname === '/cart') {
            window.location.reload();
        }
    };
    
    var selectProductPhotoListener = function (evt, productPhoto) {
        evt.preventDefault();
        evt.stopPropagation();
        const photoUrl = productPhoto.style.backgroundImage;
        const card = productPhoto.closest('.card');
        card.querySelectorAll('.agr-product-photos').forEach(item => {
            item.classList.remove('agr-photo-active');
        });
        productPhoto.classList.add('agr-photo-active');
        const cardPhoto = card.querySelector('.agr-card-image');
        cardPhoto.style.backgroundImage = photoUrl;
    };
})();

