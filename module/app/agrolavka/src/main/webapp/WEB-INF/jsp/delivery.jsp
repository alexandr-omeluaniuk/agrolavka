<%-- 
    Document   : product
    Created on : Feb 23, 2021, 10:23:56 AM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="ss.agrolavka.constants.SiteConstants"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="Условия доставки и оплаты товара" metaDescription="Условия доставки и оплаты товара в магазине Агролавка г.Дрогичин и г.Брест"
       canonical="/delivery">
    
    <jsp:body>
        
        <main class="min-vh-100">
            <div class="container">
                    
                <section class="mb-5">
                    <div class="row">
                        <div class="col-md-4 gx-5 mb-4">
                            <div class="bg-image hover-overlay ripple shadow-2-strong" data-mdb-ripple-color="light">
                                <img src="/assets/img/delivery/promo.jpg" class="img-fluid" />
                                <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                            </div>
                        </div>

                        <div class="col-md-8 gx-5 mb-4">
                            <h4><strong>Доставка товара</strong></h4>
                            
                            <hr class="my-3"/>
                            
                            <h6>Об условиях доставки</h6>
                            <small class="text-muted">
                                ${systemSettings.deliveryConditions}
                            </small>
                            
                            <hr class="my-3"/>
                            
                            <h6>Об условиях оформления заказа</h6>
                            <small class="text-muted text-justify">
                                ${systemSettings.deliveryOrder}
                            </small>
                            
                            <hr class="my-3"/>
                            
                            <h6>Способы оплаты</h6>
                            <small class="text-muted text-justify">
                                ${systemSettings.deliveryPaymentDetails}
                            </small>
                            
                        </div>
                    </div>
                </section>

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
                                ${systemSettings.discountAbout}
                            </small>

                            <hr class="my-3"/>

                            <h6>Как приобрести дисконтную карту?</h6>
                            <small class="text-muted text-justify">
                                ${systemSettings.discountParticipate}
                            </small>

                            <hr class="my-3"/>

                            <h6>От чего зависит размер скидки?</h6>
                            <small class="text-muted text-justify">
                                ${systemSettings.discountSize}
                            </small>

                        </div>
                    </div>
                </section>

                <section class="mb-5">
                    <div class="row">
                        <div class="col-md-4 gx-5 mb-4">
                            <div class="bg-image hover-overlay ripple shadow-2-strong" data-mdb-ripple-color="light">
                                <img src="/assets/img/delivery/selfpickup.jpg" class="img-fluid" />
                                <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                            </div>
                        </div>

                        <div class="col-md-8 gx-5 mb-4">
                            <h4><strong>Гарантия и возврат товара</strong></h4>

                            <hr class="my-3"/>

                            <h6>Возврат товара</h6>
                            <p class="text-muted text-justify">
                                <small>${systemSettings.returnInfo}</small>
                            </p>

                            <hr class="my-3"/>

                            <h6>Гарантия на товар</h6>
                            <p class="text-muted text-justify">
                                <small>${systemSettings.guaranteeInfo}</small>
                            </p>
                        </div>
                    </div>
                </section>
                
            </div>
        </main>
    </jsp:body>
    
</t:app>
