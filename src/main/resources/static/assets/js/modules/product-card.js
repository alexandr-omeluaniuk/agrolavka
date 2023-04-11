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

/* global mdb */

import { updateCartTotal } from './cart.js';

const addToCartListener = (evt, button) => {
    evt.preventDefault();
    evt.stopPropagation();
    const modalElement = document.getElementById('agr-add-to-cart-modal');
    modalElement.cartButton = button;
    modalElement.querySelector('input[name="productId"]').value = button.getAttribute('data-product-id');
    modalElement.querySelector('input[name="volumePrice"]').value = button.getAttribute("data-volume-price");
    modalElement.querySelector('input[name="quantity"]').value = 1;
    const modal = new mdb.Modal(modalElement, {});
    modal.toggle();
    setTimeout(() => {
        modalElement.querySelector('input[name="quantity"]').focus();
    }, 500);
};

const removeFromCartListener = (evt, button) => {
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
                updateCartTotal(cart);
                button.innerHTML = '<i class="fas fa-cart-plus me-2"></i> В корзину';
                button.classList.add('btn-outline-success');
                button.classList.remove('btn-outline-danger');
            });
        }
    }).catch(error => {
        console.error('HTTP error occurred: ' + error);
    });
};

const addToCartConfirmListener = (evt, button) => {
    const modalElement = document.getElementById('agr-add-to-cart-modal');
    var form = button.closest('.modal-content').querySelector('form');
    if (!form.checkValidity()) {
        evt.preventDefault();
        evt.stopPropagation();
    } else {
        modalElement.querySelector('button[data-mdb-dismiss]').click();
        modalElement.cartButton.setAttribute('disabled', 'true');
        const formData = {};
        form.querySelectorAll("input").forEach(input => {
            formData[input.getAttribute("name")] = input.value;
        });
        fetch('/api/agrolavka/public/cart', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        }).then(function (response) {
            if (response.ok) {
                response.json().then(cart => {
                    modalElement.cartButton.removeAttribute('disabled');
                    modalElement.cartButton.removeAttribute('data-add');
                    modalElement.cartButton.setAttribute('data-remove', '');
                    updateCartTotal(cart);
                    modalElement.cartButton.innerHTML = '<i class="fas fa-minus-circle me-2"></i> Из корзины';
                    modalElement.cartButton.classList.remove('btn-outline-success');
                    modalElement.cartButton.classList.add('btn-outline-danger');
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    }
    form.classList.add('was-validated');
};

export { addToCartListener, removeFromCartListener, addToCartConfirmListener };
