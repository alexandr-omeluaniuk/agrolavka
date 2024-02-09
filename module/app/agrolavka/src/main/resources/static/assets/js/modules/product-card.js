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
    const variantIdField = modalElement.querySelector('input[name="variantId"]');
    fieldProductId.value = button.getAttribute('data-product-id');
    modifyQuantityField(fieldQuantity, variantIdField, button);
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
    fetch('/api/agrolavka/public/cart/product/' + button.getAttribute('data-product-id'), {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    }).then(function (response) {
        if (response.ok) {
            response.json().then(cart => {
                updateCartTotal(cart);
                button.closest('x-agr-product-actions')._setInCartButtonState(false);
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
                    updateCartTotal(cart);
                    modalElement.cartButton.closest('x-agr-product-actions')._setInCartButtonState(true);
                    if (formData.variantId) {
                        modifyVariantsInCart(formData.variantId, 'add');
                    }
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
    const variantIdField = modalElement.querySelector('input[name="variantId"]');
    const fieldQuantity = modalElement.querySelector('input[name="quantity"]');
    modifyQuantityField(fieldQuantity, variantIdField, button);
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
    const quantity = btn.getAttribute("data-product-volume-quantity");
    btn.closest('div[data-volumes]').setAttribute("data-selected-volume-quantity", quantity);
    btn.closest('div[data-volumes]').setAttribute("data-selected-volume-price", price);
    const priceBig = parseFloat(price).toFixed(2).split('.')[0];
    const priceSmall = parseFloat(price).toFixed(2).split('.')[1];
    let container = btn.closest('.card');
    if (!container) {
        container = btn.closest('.row');
    }
    container.querySelector('.agr-price').innerHTML = priceBig + ".<small>" + priceSmall
            + '</small> <small class="text-muted">BYN</small>';
};

const photoClickListener = (evt, image) => {
    evt.preventDefault();
    evt.stopPropagation();
    const modalElement = document.getElementById('agr-photo-modal');
    const modal = new mdb.Modal(modalElement, {});
    modal.toggle();
};

const modifyVariantsInCart = (variantId, action) => {
    const productVariantsComponent = document.querySelector('[data-product-variant-id="' + variantId + '"]')
        .closest('x-agr-product-variants');
    const inCartVariants = JSON.parse(productVariantsComponent.getAttribute("data-variants-in-cart"));
    if (action === 'add') {
        inCartVariants.push(variantId);
    } else {
        inCartVariants = inCartVariants.filter(v => v !== variantId);
    }
    productVariantsComponent.setAttribute("data-variants-in-cart", JSON.stringify(inCartVariants));
};

const modifyQuantityField = (fieldQuantity, variantIdField, button) => {
    const volumesComponent = button.closest('div').querySelector('div[data-volumes]');
    const variantsComponent = button.closest('div').querySelector('div[data-selected-variant-id]');
    const helpText = fieldQuantity.closest('form').querySelector('.agr-volume-help-text');
    if (variantsComponent) {
        variantIdField.value = variantsComponent.getAttribute("data-selected-variant-id");
        fieldQuantity.removeAttribute("step");
        fieldQuantity.setAttribute("min", "1");
        fieldQuantity.value = 1;
        helpText.classList.add('d-none');
    } else if (volumesComponent) {
        fieldQuantity.setAttribute("step", ".1");
        try {
            const volumes = volumesComponent.getAttribute("data-volumes");
            const pricesList = JSON.parse(volumes.replaceAll("'", '"'));
            const minVolume = pricesList.reduce((prev, curr) => {
                return prev.amount < curr.amount ? prev : curr;
            });
            const minAmount = minVolume.amount >= 1 ? parseFloat(minVolume.amount).toFixed(0) : minVolume.amount;
            fieldQuantity.setAttribute("min", minAmount);
            fieldQuantity.setAttribute("step", minAmount);
            const valQuantity = volumesComponent.getAttribute("data-selected-volume-quantity");
            fieldQuantity.value = valQuantity >= 1 ? parseFloat(valQuantity).toFixed(0) : valQuantity;
            helpText.classList.remove('d-none');
            helpText.querySelector('b').innerHTML = minAmount + 'л';
        } catch (e) {
            console.warn(e);
        }
    } else {
        fieldQuantity.removeAttribute("step");
        fieldQuantity.setAttribute("min", "1");
        fieldQuantity.value = 1;
        helpText.classList.add('d-none');
    }
    const quantityType = fieldQuantity.closest('div').querySelector('span');
    quantityType.innerHTML = volumesComponent ? 'литр.' : 'шт.';
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
