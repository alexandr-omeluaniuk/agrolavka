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
        <main class="container">
            <h3 class="fw-bold mb-5 mt-2 text-center">Оформление заказа</h3>
            <div class="row">
                <div class="col-sm-12 col-md-9 mb-4">

                    <form class="needs-validation" novalidate id="order-form">
                        <fieldset>
                            <legend class="col-form-label pt-0 fw-bolder mb-2">
                                Контактные данные
                            </legend>
                            <div class="input-group mb-3 has-validation">
                                <span class="input-group-text" id="order-phone-number"><i class="fas fa-phone-alt"></i></span>
                                <input type="text" class="form-control" placeholder="Номер телефона" required
                                       aria-label="Номер телефона" aria-describedby="order-phone-number"
                                       data-format="* (***) ***-**-**" data-mask="_ (___) ___-__-__"
                                       pattern="8\s\([0-9][0-9][0-9]\)\s[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]">
                                <div class="invalid-feedback">
                                    Обязательно для заполнения
                                </div>
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
                            <div class="mt-3 d-none">
                                <div class="row">
                                    <div class="col-sm-12 col-md-9">
                                        <div class="form-floating mb-3">
                                            <input type="text" class="form-control" id="address-city" placeholder="Брест">
                                            <label for="address-city">Населенный пункт</label>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 col-md-3">
                                        <div class="form-floating mb-3">
                                            <input type="text" class="form-control" id="address-postcode" placeholder="224033">
                                            <label for="address-postcode">Почтовый индекс</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-12 col-md-6">
                                        <div class="form-floating mb-3">
                                            <input type="text" class="form-control" id="address-street" placeholder="Рябиновая">
                                            <label for="address-street">Улица</label>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 col-md-3">
                                        <div class="form-floating mb-3">
                                            <input type="text" class="form-control" id="address-house" placeholder="31">
                                            <label for="address-house">Дом</label>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 col-md-3">
                                        <div class="form-floating mb-3">
                                            <input type="text" class="form-control" id="address-flat" placeholder="5555">
                                            <label for="address-flat">Квартира</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                    </form>

                </div>
                <div class="col-sm-12 col-md-3 mb-4">

                    <div class="card">
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
                            <hr/>
                            <div class="d-flex justify-content-between">
                                <span class="text-muted">Доставка</span>
                                <div>
                                    <span class="text-dark fw-bold me-2">
                                        0.<small>00</small>
                                        <small class="text-muted">BYN</small>
                                    </span>
                                </div>
                            </div>
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

        document.querySelector("#order-confirm").addEventListener('click', function (event) {
            var form = document.querySelector('#order-form');
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            }
            form.classList.add('was-validated');
        });
    })();
</script>
