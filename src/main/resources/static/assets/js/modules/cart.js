/* 
 * Copyright (C) 2023 alex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import { addElementEvent } from './util-functions.js';

const updateCartTotal = (cart) => {
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

const cartRemoveProductListener = (evt, button) => {
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

const cartProductQuantityChangeListener = (evt, input) => {
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

const cartQuantityPlusBtnListener = (evt, element) => {
    const input = element.closest("div").querySelector("[data-product-quantity]");
    input.value = parseInt(input.value) + 1;
    cartProductQuantityChangeListener(evt, input);
};

const cartQuantityMinusBtnListener = (evt, element) => {
    const input = element.closest("div").querySelector("[data-product-quantity]");
    input.value = parseInt(input.value) > 1 ? parseInt(input.value) - 1 : parseInt(input.value);
    cartProductQuantityChangeListener(evt, input);
};

const handleCartClickEvent = (evt) => {
    addElementEvent(evt, "[data-product-id][data-remove-product-from-cart]", cartRemoveProductListener);
    addElementEvent(evt, "[data-product-quantity-plus]", cartQuantityPlusBtnListener);
    addElementEvent(evt, "[data-product-quantity-minus]", cartQuantityMinusBtnListener);
};

const handleCartChangeEvent = (evt) => {
    addElementEvent(evt, "[data-product-id][data-product-quantity]", cartProductQuantityChangeListener);
};

export { updateCartTotal, handleCartClickEvent, handleCartChangeEvent }
