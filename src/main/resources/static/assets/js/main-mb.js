/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global parseFloat, fetch, mdb */

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
        const modal = evt.target.closest('#agr-photo-modal');
        if (modal) {
            if (!evt.target.classList.contains('btn-close')) {
                evt.stopPropagation();
                return;
            }
        }
        const agrVolumeBtn = evt.target.closest('.agr-volume-btn');
        if (agrVolumeBtn) {
            agrVolumeBtnListener(evt, agrVolumeBtn);
        }
        const agrClickedPhoto = evt.target.closest('.agr-product-image-carousel');
        if (agrClickedPhoto) {
            photoClickListener(evt, agrClickedPhoto);
        }
        const addToCartBtn = evt.target.closest("[data-product-id][data-add]");
        if (addToCartBtn) {
            addToCartListener(evt, addToCartBtn);
        }
        const addToCartConfirmBtn = evt.target.closest("[data-add-to-cart-confirm]");
        if (addToCartConfirmBtn) {
            addToCartConfirmListener(evt, addToCartConfirmBtn);
        }
        const removeFromCartCartBtn = evt.target.closest("[data-product-id][data-remove]");
        if (removeFromCartCartBtn) {
            removeFromCartListener(evt, removeFromCartCartBtn);
        }
        const orderOneClickButton = evt.target.closest("[data-product-id][data-order]");
        if (orderOneClickButton) {
            orderOneClickButtonListener(evt, orderOneClickButton);
        }
        const orderOneClickConfirmButton = evt.target.closest("button[data-one-click-order]");
        if (orderOneClickConfirmButton) {
            orderOneClickConfirmButtonListener(evt, orderOneClickConfirmButton);
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
    
    var addToCartConfirmListener = function (evt, button) {
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
                        _updateCartTotal(cart);
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
        const volumePrice = button.getAttribute('data-volume-price');
        const modalElement = document.getElementById('agr-one-click-order-modal');
        modalElement.querySelector('input[name="productId"]').value = productId;
        modalElement.querySelector('input[name="volumePrice"]').value = volumePrice;
        modalElement.querySelector('input[name="quantity"]').value = 1;
        const confirmButton = modalElement.querySelector('button[data-one-click-order]');
        confirmButton.removeAttribute('disabled');
        confirmButton.querySelector('.spinner-border').classList.add('d-none');
        const modal = new mdb.Modal(modalElement, {});
        modal.toggle();
        setTimeout(() => {
            modalElement.querySelector('input[name="phone"]').focus();
        }, 500);
    };
    
    var photoClickListener = function(evt, image) {
        evt.preventDefault();
        evt.stopPropagation();
        const modalElement = document.getElementById('agr-photo-modal');
        const modal = new mdb.Modal(modalElement, {});
        modal.toggle();
    };
    
    var agrVolumeBtnListener = function(evt, btn) {
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
        const addToCartBtn = container.querySelector('button[data-add]');
        if (addToCartBtn) {
            addToCartBtn.setAttribute('data-volume-price', price);
        }
        const buyNowBtn = container.querySelector('button[data-order]');
        if (buyNowBtn) {
            buyNowBtn.setAttribute('data-volume-price', price);
        }
    };
    
    var orderOneClickConfirmButtonListener = function(evt, button) {
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

