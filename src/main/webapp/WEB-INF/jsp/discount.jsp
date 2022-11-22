<%-- 
    Document   : product
    Created on : Feb 23, 2021, 10:23:56 AM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="Дисконтная программа" metaDescription="Условия дисконтной программы, акции и скидки в магазине Агролавка г.Дрогичин и г.Брест"
       canonical="/discount">
    
    <jsp:body>
        
        <main class="min-vh-100">
            <div class="container">
                
                <section class="mb-5">
                    <div class="row">
                        <div class="col-md-4 gx-5 mb-4">
                            <div class="bg-image hover-overlay ripple shadow-2-strong" data-mdb-ripple-color="light">
                                <img src="/assets/img/intro/discount.webp" class="img-fluid" />
                                <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                            </div>
                        </div>

                        <div class="col-md-8 gx-5 mb-4">
                            <h4><strong>Дисконтная программа</strong></h4>
                            
                            <hr class="my-3"/>
                            
                            <h6>Что такое дисконтная программа магазина «Агролавка»?</h6>
                            <small class="text-muted">
                                Наша дисконтная программа — это возможность получать скидки от 3% до 7% от общей суммы покупок во всех торговых точках.
                            </small>
                            
                            <hr class="my-3"/>
                            
                            <h6>Как приобрести дисконтную карту?</h6>
                            <small class="text-muted">
                                Приобрести дисконтную карту может каждый вне зависимости от суммы чека. Нужно просто заполнить анкету в магазине «Агролавка».
                            </small>
                            
                        </div>
                    </div>
                    
                    <hr class="my-3"/>
                            
                    <h6>От чего зависит размер скидки?</h6>
                    <small class="text-muted">
                        Начальная скидка по дисконтной программе - 3%. Размер скидки может быть увеличен в зависимости от обьема заказов и их периодичности.
                    </small>
                    
                </section>
                
            </div>
        </main>
    </jsp:body>
    
</t:app>
