<%-- 
    Document   : product
    Created on : Feb 23, 2021, 10:23:56 AM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="Условия доставки и оплаты товара" metaDescription="Условия доставки и оплаты товара в магазине Агролавка г.Дрогичин"
       canonical="/delivery">
    
    <jsp:body>
        
        <main class="min-vh-100">
            <div class="container">
                    
                <section>
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
                                Заявки обрабатываются ежедневно с 09:00 - 20:00.<br/> 
                                Курьерская доставка осуществляется ежедневно по предварительной договоренности.<br/>
                                Пересылка почтой осуществляется с понедельника по пятницу.
                            </small>
                            
                            <hr class="my-3"/>
                            
                            <h6>Об условиях оформления заказа</h6>
                            <small class="text-muted text-justify">
                                Вся информация, предоставляемая нам при оформлении заказа, остается конфиденциальной и не передается третьим лицам. 
                                Доставку продукции мы производим только по факту подтверждения заказа по телефону и согласования удобного дня и времени доставки.
                                <br/><span class="text-dark">Заказы, не получившие подтверждение по телефону, к исполнению не принимаются.</span>
                            </small>
                            
                            <hr class="my-3"/>
                            
                            <h6>Способы оплаты</h6>
                            <ul class="list-unstyled">
                                <li><small class="text-muted">Наличными деньгами (при самовывозе, доставке курьером по Беларуси).</small></li>
                                <li><small class="text-muted">Банковской картой через терминал (при самовывозе).</small></li>
                                <li><small class="text-muted">Наложенным платежом (при доставке почтой по Беларуси).</small></li>
                            </ul>
                            <small class="text-dark">Оплата принимается только в белорусских рублях.</small>
                            
                        </div>
                    </div>
                    
                    <hr class="my-3"/>
                            
                    <h3 class="text-center">Способы и регионы доставки товаров</h3>
                    
                    <div class="row">
                        <div class="col-sm-12 col-md-4">
                            <div class="card">
                                <div class="card-header bg-white py-3">
                                    <p class="text-uppercase small mb-2"><strong>Самовывоз</strong></p>
                                    <h5 class="mb-0">Бесплатно</h5>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                </section>
                
            </div>
        </main>
    </jsp:body>
    
</t:app>
