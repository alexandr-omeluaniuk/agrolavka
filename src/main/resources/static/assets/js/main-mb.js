/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global parseFloat, fetch, mdb */

import { handleProductCardEvent } from "./modules/product-card.js";
import { updateCartTotal } from './modules/cart.js';
import { initScrollEvents} from './modules/scroll-events.js';
import { handleMenuEvent } from './modules/menu.js';

(function () {
    "use strict";

    initScrollEvents();
    
    document.querySelector('body').addEventListener('click', function(evt) {
        const modal = evt.target.closest('#agr-photo-modal');
        if (modal) {
            if (!evt.target.classList.contains('btn-close') 
                    && !evt.target.classList.contains('swiper-button-next')
                    && !evt.target.classList.contains('swiper-button-prev')) {
                evt.stopPropagation();
                return;
            }
        }
        const agrNavBackBtn = evt.target.closest('.agr-nav-back-btn');
        if (agrNavBackBtn) {
            navBackBtnClick(evt, agrNavBackBtn);
        }
        handleMenuEvent(evt);
        handleProductCardEvent(evt);
        
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
    
    var navBackBtnClick = function(evt, button) {
        const lastCatalogLink = document.querySelector('#agr-last-catalog-link');
        if (lastCatalogLink) {
            lastCatalogLink.click();
        } else if (window.location.pathname === '/catalog') {
            window.location.href = '/';
        } else {
            window.location.href = '/catalog';
        }
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
                    
                    updateCartTotal(cart);
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    };
    
    var cartProductQuantityChangeListener = function (evt, input) {
        let quantity = input.value;
        quantity = Math.round(quantity);
        input.value = quantity;
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
                    updateCartTotal(cart);
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    };
    
})();

