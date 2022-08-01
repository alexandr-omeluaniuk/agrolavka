<%-- 
    Document   : order
    Created on : Apr 7, 2021, 8:33:37 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" import="ss.entity.agrolavka.*"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="order" tagdir="/WEB-INF/tags/order" %>
<!DOCTYPE html>
<t:app title="Оформление заказа" metaDescription="Оформление заказа в магазине Агролавка" canonical="/order">

    <jsp:body>
        <main class="min-vh-100">
            <div class="container">
                <section>
                    <h3 class="text-center mb-4">Оформление заказа</h3>
                    <c:choose>
                        <c:when test="${cart.positions.size() == 0}">
                            <order:success-message/>
                        </c:when>    
                        <c:otherwise>
                            <div class="row">
                                <div class="col-sm-12 col-md-9 mb-4">
                                    <form class="needs-validation shadow-1-strong p-4" novalidate id="order-form">
                                        <order:form-common-section/>
                                        <fieldset>
                                            <order:delivery-type totalInteger="${totalInteger}"/>
                                            <order:delivery-post/>
                                            <order:delivery-europost europostLocations="${europostLocations}"/>
                                        </fieldset>
                                    </form>
                                </div>
                                <div class="col-sm-12 col-md-3 mb-4">
                                    <order:total-info-card totalDecimal="${totalDecimal}" totalInteger="${totalInteger}"/>
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
                const europost = document.querySelector('#order-europost');
                if (event.target.id === "self-delivery") {
                    europost.classList.add("d-none");
                    address.classList.add("d-none");
                    address.querySelectorAll('input[required]').forEach(input => {
                        input.setAttribute("readonly", "true");
                    });
                    europost.querySelectorAll('input[required]').forEach(input => {
                        input.setAttribute("readonly", "true");
                    });
                } else if (event.target.id === "remote-delivery") {
                    europost.classList.add("d-none");
                    address.classList.remove("d-none");
                    address.querySelectorAll('input[required]').forEach(input => {
                        input.removeAttribute("readonly");
                    });
                    europost.querySelectorAll('input[required]').forEach(input => {
                        input.setAttribute("readonly", "true");
                    });
                } else if (event.target.id === "europost-delivery") {
                    address.classList.add("d-none");
                    europost.classList.remove("d-none");
                    europost.querySelectorAll('input[required]').forEach(input => {
                        input.removeAttribute("readonly");
                    });
                    address.querySelectorAll('input[required]').forEach(input => {
                        input.setAttribute("readonly", "true");
                    });
                }
            });
        });
        
        const selfDeliveryRadio = document.querySelector('input[name="delivery"][id="self-delivery"]');
        if (selfDeliveryRadio) {
            selfDeliveryRadio.dispatchEvent(new Event('change'));
        }
    })();
</script>
