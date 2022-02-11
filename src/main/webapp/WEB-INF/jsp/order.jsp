<%-- 
    Document   : order
    Created on : Apr 7, 2021, 8:33:37 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" import="ss.entity.agrolavka.*"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<t:app title="Оформление заказа" metaDescription="Оформление заказа в магазине Агролавка" canonical="/order">

    <jsp:body>
        <main class="min-vh-100">
            <div class="container">
                <section>
                    <h3 class="text-center mb-4">Оформление заказа</h3>
                    
                    <c:choose>
                        <c:when test="${cart.positions.size() == 0}">
                            <div class="alert alert-success" role="alert">
                                <h5><i class="fas fa-check-circle me-2"></i>Большое спасибо за Ваш заказ!</h5>
                                <hr/>
                                <p>
                                    В ближайшее время наш оператор перезвонит Вам по указанному Вами номеру, чтобы уточнить детали заказа.
                                </p>
                                <div class="d-flex justify-content-end">
                                    <a href="/" class="agr-link">На главную <i class="fas fa-chevron-right ms-2"></i></a>
                                </div>
                            </div>
                        </c:when>    
                        <c:otherwise>
                            <div class="row">
                                <div class="col-sm-12 col-md-9 mb-4">

                                    <form class="needs-validation shadow-1-strong p-4" novalidate id="order-form">
                                        <fieldset>
                                            <legend class="col-form-label pt-0 fw-bolder mb-2">
                                                Контактные данные
                                            </legend>
                                            <label for="order-mobile" class="mb-1">Номер мобильного телефона</label>
                                            <div class="input-group mb-3">
                                                <span class="input-group-text">+375</span>
                                                <input type="text" class="form-control" placeholder="Номер телефона" required
                                                   data-format="(**) ***-**-**" data-mask="(__) ___-__-__" id="order-mobile"
                                                   pattern="\([0-9][0-9]\)\s[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]"
                                                   name="phone">
                                            </div>
                                            
                                            <div class="mb-3">
                                                <label for="comment">Комментарий к заказу</label>
                                                <textarea type="text" class="form-control" id="comment" name="comment"></textarea>
                                            </div>
                                            
                                        </fieldset>
                                        <fieldset>
                                            <legend class="col-form-label pt-0 fw-bolder mb-2">Доставка</legend>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="delivery" id="self-delivery" checked>
                                                <label class="form-check-label" for="self-delivery">
                                                    Самовывоз из магазина Агролавка
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="delivery" id="remote-delivery">
                                                <label class="form-check-label" for="remote-delivery">
                                                    Доставка по адресу
                                                </label>
                                            </div>
                                            <div class="mt-3 d-none" id="order-address">
                                                <small class="text-muted mb-2 ms-2">* фамилия имя и отчество заполняются только если планируется доставка почтой</small>
                                                <div class="row">
                                                    <div class="col-sm-12 col-lg-4">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="lastname" placeholder="Иванов"
                                                                   name="lastname">
                                                            <label for="lastname">Фамилия</label>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-12 col-lg-4">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="firstname" placeholder="Иван"
                                                                   name="firstname">
                                                            <label for="firstname">Имя</label>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-12 col-lg-4">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="middlename" placeholder="Иванович"
                                                                   name="middlename">
                                                            <label for="middlename">Отчество</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-12 col-md-6">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="address-region" placeholder="Брестская" required
                                                                   readonly name="region">
                                                            <label for="address-region">Область *</label>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-12 col-md-6">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="address-district" placeholder="Кобринский"
                                                                   readonly required name="district">
                                                            <label for="address-district">Район *</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-12 col-md-9">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="address-city" placeholder="Брест" required
                                                                   readonly name="city">
                                                            <label for="address-city">Населенный пункт *</label>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-12 col-md-3">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="address-postcode" placeholder="224033"
                                                                   name="postcode">
                                                            <label for="address-postcode">Почтовый индекс</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-12 col-md-6">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="address-street" placeholder="Рябиновая" required
                                                                   readonly name="street">
                                                            <label for="address-street">Улица *</label>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-12 col-md-3">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="address-house" placeholder="31" required
                                                                   readonly name="house">
                                                            <label for="address-house">Дом *</label>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-12 col-md-3">
                                                        <div class="form-floating mb-3">
                                                            <input type="text" class="form-control" id="address-flat" placeholder="5555" name="flat">
                                                            <label for="address-flat">Квартира</label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </fieldset>
                                    </form>

                                </div>
                                <div class="col-sm-12 col-md-3 mb-4">

                                    <div class="card shadow-1-strong">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between">
                                                <span class="text-muted">Товаров на</span>
                                                <div>
                                                    <span class="text-dark fw-bold me-2">
                                                        ${totalInteger}.<small>${totalDecimal}</small>
                                                        <small class="text-muted">BYN</small>
                                                    </span>
                                                </div>
                                            </div>
                                            <!--hr/>
                                            <div class="d-flex justify-content-between">
                                                <span class="text-muted">Доставка</span>
                                                <div>
                                                    <span class="text-dark fw-bold me-2">
                                                        0.<small>00</small>
                                                        <small class="text-muted">BYN</small>
                                                    </span>
                                                </div>
                                            </div-->
                                            <hr/>
                                            <div class="d-flex justify-content-between">
                                                <span class="text-dark">Итого</span>
                                                <div>
                                                    <span class="text-dark fw-bold me-2">
                                                        ${totalInteger}.<small>${totalDecimal}</small>
                                                        <small class="text-muted">BYN</small>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <button class="btn btn-success w-100 mt-4" id="order-confirm">
                                        <i class="fas fa-check me-2"></i>Заказать
                                    </button>

                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    
                </section>
            </div>
            
        </main>
    </jsp:body>

</t:app>

<script>
    (function () {
        "use strict";

        function doFormat(x, pattern, mask) {
            var strippedValue = x.replace(/[^0-9]/g, "");
            var chars = strippedValue.split('');
            var count = 0;

            var formatted = '';
            for (var i = 0; i < pattern.length; i++) {
                const c = pattern[i];
                if (chars[count]) {
                    if (/\*/.test(c)) {
                        formatted += chars[count];
                        count++;
                    } else {
                        formatted += c;
                    }
                } else if (mask) {
                    if (mask.split('')[i])
                        formatted += mask.split('')[i];
                }
            }
            return formatted;
        }

        document.querySelectorAll('[data-mask]').forEach(function (e) {
            function format(elem) {
                const val = doFormat(elem.value, elem.getAttribute('data-format'));
                elem.value = doFormat(elem.value, elem.getAttribute('data-format'), elem.getAttribute('data-mask'));

                if (elem.createTextRange) {
                    var range = elem.createTextRange();
                    range.move('character', val.length);
                    range.select();
                } else if (elem.selectionStart) {
                    elem.focus();
                    elem.setSelectionRange(val.length, val.length);
                }
            }
            e.addEventListener('keyup', function () {
                format(e);
            });
            format(e)
        });
        const submitOrderButton = document.querySelector("#order-confirm");
        if (submitOrderButton) {
            submitOrderButton.addEventListener('click', function (event) {
                var form = document.querySelector('#order-form');
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                } else {
                    submitOrderButton.setAttribute('disabled', 'true');
                    const formData = {};
                    form.querySelectorAll("input").forEach(input => {
                        formData[input.getAttribute("name")] = input.value;
                    });
                    form.querySelectorAll("textarea").forEach(input => {
                        formData[input.getAttribute("name")] = input.value;
                    });
                    fetch('/api/agrolavka/public/order', {
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(formData)
                    }).then(function (response) {
                        if (response.ok) {
                            response.json().then(json => {
                                window.location.reload();
                            });
                        }
                    }).catch(error => {
                        console.error('HTTP error occurred: ' + error);
                    });
                }
                form.classList.add('was-validated');
            });
        }

        document.querySelectorAll('input[name="delivery"]').forEach(el => {
            el.addEventListener('change', function (event) {
                const address = document.querySelector('#order-address');
                if (event.target.id === "self-delivery") {
                    address.classList.add("d-none");
                    address.querySelectorAll('input[required]').forEach(input => {
                        input.setAttribute("readonly", "true");
                    });
                } else {
                    address.classList.remove("d-none");
                    address.querySelectorAll('input[required]').forEach(input => {
                        input.removeAttribute("readonly");
                    });
                }
            });
        });
    })();
</script>
