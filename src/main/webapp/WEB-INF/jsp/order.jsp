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
                <div class="col-sm-12 col-md-9">

                    <form>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="delivery" id="self-delivery">
                            <label class="form-check-label fw-bold" for="self-delivery">
                                Самовывоз из магазина Агролавка
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="delivery" id="remote-delivery" checked>
                            <label class="form-check-label fw-bold" for="remote-delivery">
                                Доставка
                            </label>
                        </div>
                        <fieldset>
                            <hr/>
                            <legend class="col-form-label col-sm-2 pt-0 fw-bolder">Адрес доставки</legend>
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
                        </fieldset>
                    </form>

                </div>
                <div class="col-sm-12 col-md-3">

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

                    <button class="btn btn-success w-100 mt-4">
                        <i class="fas fa-check me-2"></i>Заказать
                    </button>

                </div>
            </div>

        </main>
    </jsp:body>

</t:app>
