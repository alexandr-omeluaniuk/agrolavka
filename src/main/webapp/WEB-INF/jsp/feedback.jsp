<%-- 
    Document   : catalog
    Created on : Feb 18, 2021, 9:04:42 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="Напишите нам" metaDescription="Форма обратной связи" canonical="/feedback">

    <jsp:body>
        <main class="min-vh-100">
            <div class="container mb-5">
                
                <section id="feedback-success" class="d-none">
                    <div class="alert alert-success" role="alert">
                        <h5><i class="fas fa-check-circle me-2"></i>Большое спасибо за Ваш отзыв!</h5>
                        <hr/>
                        <p>
                            Мы примем к сведению Ваше замечание (или предложение) и в ближайшее время рассмотрим его.
                        </p>
                        <div class="d-flex justify-content-end">
                            <a href="/" class="agr-link">На главную <i class="fas fa-chevron-right ms-2"></i></a>
                        </div>
                    </div>
                </section>

                <section>
                    <h3 class="text-center mb-4">Написать нам</h3>
                    <p class="text-uppercase text-muted text-center">
                        Помогите улучшить наш сайт
                    </p>
                </section>

                <section class="m-2">
                    <form class="needs-validation shadow-1-strong p-4" novalidate id="feedback-form">
                        <fieldset>
                            <legend class="col-form-label pt-0 fw-bolder mb-2">
                                Форма обратной связи
                            </legend>

                            <div class="mb-3">
                                <label for="feedback-message">Сообщение</label>
                                <textarea type="text" class="form-control" id="feedback-message" name="message" rows="9" required maxlength="10000"></textarea>
                            </div>

                            <div class="mb-3">
                                <label for="feedback-contact" class="mb-1">Eсли вы желаете чтобы мы вам ответили - оставьте ваш контакт</label>
                                <input type="text" class="form-control" placeholder="Номер телефона или Email" 
                                       id="feedback-contact" name="contact" maxlength="255">
                            </div>

                        </fieldset>

                        <button class="btn btn-success w-100 mt-4" id="feedback-confirm">
                            <i class="fas fa-check me-2"></i>Отправить
                        </button>

                    </form>
                </section>
            </div>
        </main>
    </jsp:body>

</t:app>

<script>
    (function () {
        "use strict";

        const submitFeedbackButton = document.querySelector("#feedback-confirm");
        if (submitFeedbackButton) {
            submitFeedbackButton.addEventListener('click', function (event) {
                var form = document.querySelector('#feedback-form');
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                } else {
                    submitFeedbackButton.setAttribute('disabled', 'true');
                    const formData = {};
                    form.querySelectorAll("input").forEach(input => {
                        formData[input.getAttribute("name")] = input.value;
                    });
                    form.querySelectorAll("textarea").forEach(input => {
                        formData[input.getAttribute("name")] = input.value;
                    });
                    fetch('/api/agrolavka/public/feedback', {
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(formData)
                    }).then(function (response) {
                        if (response.ok) {
                            response.json().then(json => {
                                document.querySelectorAll('section').forEach(section => {
                                    section.classList.add('d-none');
                                });
                                document.querySelector('#feedback-success').classList.remove('d-none');
                            });
                        }
                    }).catch(error => {
                        console.error('HTTP error occurred: ' + error);
                    });
                }
                form.classList.add('was-validated');
            });
        }
    })();
</script>
