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
import { addElementEvent } from './util-functions.js';

const addToCartListener = (evt, button) => {
    evt.preventDefault();
    evt.stopPropagation();
    const modalElement = document.getElementById('agr-add-to-cart-modal');
    modalElement.cartButton = button;
    const fieldProductId = modalElement.querySelector('input[name="productId"]');
    const fieldQuantity = modalElement.querySelector('input[name="quantity"]');
    fieldProductId.value = button.getAttribute('data-product-id');
    const volumes = button.getAttribute('data-volumes');
    modifyQuantityField(volumes, fieldQuantity, button);
    const modal = new mdb.Modal(modalElement, {});
    modal.toggle();
    setTimeout(() => {
        fieldQuantity.focus();
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

const orderOneClickButtonListener = (evt, button) => {
    evt.preventDefault();
    evt.stopPropagation();
    const productId = button.getAttribute('data-product-id');
    const modalElement = document.getElementById('agr-one-click-order-modal');
    modalElement.querySelector('input[name="productId"]').value = productId;
    const fieldQuantity = modalElement.querySelector('input[name="quantity"]');
    const volumes = button.getAttribute('data-volumes');
    modifyQuantityField(volumes, fieldQuantity, button);
    const confirmButton = modalElement.querySelector('button[data-one-click-order]');
    confirmButton.removeAttribute('disabled');
    confirmButton.querySelector('.spinner-border').classList.add('d-none');
    const modal = new mdb.Modal(modalElement, {});
    modal.toggle();
    setTimeout(() => {
        modalElement.querySelector('input[name="phone"]').focus();
    }, 500);
};

const orderOneClickConfirmButtonListener = (evt, button) => {
    var form = button.closest('.modal-content').querySelector('form');
    if (!form.checkValidity()) {
        evt.preventDefault();
        evt.stopPropagation();
    } else {
        button.setAttribute('disabled', 'true');
        const formData = {};
        form.querySelectorAll("input").forEach(input => {
            formData[input.getAttribute("name")] = input.value;
        });
        button.querySelector('.spinner-border').classList.remove('d-none');
        fetch('/api/agrolavka/public/order/one-click', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        }).then(function (response) {
            if (response.ok) {
                response.json().then(json => {
                    const modalElement = document.getElementById('agr-one-click-order-modal');
                    modalElement.querySelector('button[data-mdb-dismiss]').click();
                    const toast = document.querySelector('#agr-one-click-order-success');
                    toast.style.display = 'block';
                    toast.classList.add('show');
                    setTimeout(() => {
                        toast.classList.remove('show');
                        setTimeout(() => {
                            toast.style.display = 'none';
                        });
                    }, 2000);
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    }
    form.classList.add('was-validated');
};

const productVolumeClickListener = (evt, btn) => {
    evt.preventDefault();
    evt.stopPropagation();
    btn.closest('.btn-group').querySelectorAll('button').forEach(b => {
        b.classList.add("btn-outline-info");
        b.classList.remove("btn-info");
    });
    btn.classList.remove("btn-outline-info");
    btn.classList.add("btn-info");
    const price = btn.getAttribute("data-product-volume-price");
    const priceBig = parseFloat(price).toFixed(2).split('.')[0];
    const priceSmall = parseFloat(price).toFixed(2).split('.')[1];
    let container = btn.closest('.card');
    if (!container) {
        container = btn.closest('.row');
    }
    container.querySelector('.agr-price').innerHTML = priceBig + ".<small>" + priceSmall
            + '</small> <small class="text-muted">BYN</small>';
//    const addToCartBtn = container.querySelector('button[data-add]');
//    if (addToCartBtn) {
//        addToCartBtn.setAttribute('data-volume-price', price);
//    }
//    const removeFromCartBtn = container.querySelector('button[data-remove]');
//    if (removeFromCartBtn) {
//        removeFromCartBtn.setAttribute('data-volume-price', price);
//    }
//    const buyNowBtn = container.querySelector('button[data-order]');
//    if (buyNowBtn) {
//        buyNowBtn.setAttribute('data-volume-price', price);
//    }
};

const photoClickListener = (evt, image) => {
    evt.preventDefault();
    evt.stopPropagation();
    const modalElement = document.getElementById('agr-photo-modal');
    const modal = new mdb.Modal(modalElement, {});
    modal.toggle();
};

const modifyQuantityField = (volumes, fieldQuantity, button) => {
    if (volumes) {
        fieldQuantity.setAttribute("step", ".1");
        try {
            const pricesList = JSON.parse(volumes.replaceAll("'", '"'));
            const minVolume = pricesList.reduce((prev, curr) => {
                return prev.v.replace('л', '') < curr.v.replace('л', '') ? prev : curr;
            });
            fieldQuantity.setAttribute("min", minVolume.v.replace('л', ''));
            fieldQuantity.value = button.closest('div')
                .querySelector('[aria-label="Volumes"]')
                .querySelector('.btn-info')
                .innerHTML.replace('л', '').replace(',', '.');
        } catch (e) {
            console.warn(e);
        }
    } else {
        fieldQuantity.removeAttribute("step");
        fieldQuantity.setAttribute("min", "1");
        fieldQuantity.value = 1;
    }
};

export const handleProductCardEvent = (evt) => {
    addElementEvent(evt, "[data-product-id][data-add]", addToCartListener);
    addElementEvent(evt, "[data-product-id][data-remove]", removeFromCartListener);
    addElementEvent(evt, "[data-add-to-cart-confirm]", addToCartConfirmListener);
    addElementEvent(evt, "[data-product-id][data-order]", orderOneClickButtonListener);
    addElementEvent(evt, "button[data-one-click-order]", orderOneClickConfirmButtonListener);
    addElementEvent(evt, ".agr-volume-btn", productVolumeClickListener);
    addElementEvent(evt, ".agr-product-image-carousel", photoClickListener);
};
