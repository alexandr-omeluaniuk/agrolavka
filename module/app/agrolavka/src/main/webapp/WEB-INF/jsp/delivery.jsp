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
                                Заявки обрабатываются ежедневно с 09:00 - 20:00.<br/> 
                                Минимальная сумма заказа для доставки - <b>25 рублей</b><br/>
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
                            
                    <h3 class="text-center mb-4 mt-4">Способы и регионы доставки товаров</h3>
                    
                    <div class="row">
                        
                        <div class="col-sm-12 col-md-4">
                            <div class="card shadow-1-strong mb-3">
                                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                                    <h5 class="mb-3 mt-3 text-center text-dark"><strong>Доставка Европочтой</strong></h5>
                                    <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/delivery/europost.jpg')"></div>
                                    <div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div>
                                    
                                </div>
                                <div class="card-body">
                                    <small class="mb-0 text-dark">Доставка посылок по всей Беларуси, 
                                        <a class="agr-link" href="https://evropochta.by/">Европочтой</a>
                                        имеет ряд преимуществ, таких как низкие тарифы для физических и юридических лиц,
                                        более 230 офисов по всей Беларуси
                                    </small>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-sm-12 col-md-4">
                            <div class="card shadow-1-strong mb-3">
                                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                                    <h5 class="mb-3 mt-3 text-center text-dark"><strong>Почта</strong></h5>
                                    <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/delivery/post.jpg')"></div>
                                    <div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div>
                                    <div class="card-body">
                                        <p class="text-justify small">
                                            Во все регионы Беларуси через отделения РУП «Белпочта». 
                                            Срок доставки: 2-4 рабочих дня с момента отправки посылки, после полной ее комплектации. 
                                            Отправка осуществляется с понедельника по пятницу.
                                            Стоимость доставки зависит от почтовых тарифов.
                                            Оплата при отправке заказа почтой: производится в белорусских рублях и осуществляется на почте при 
                                            получении заказа. Срок сохранения посылки на почте: первые 7 дней – бесплатно, далее хранение платно, 
                                            после хранения 30 дней – посылка уходит обратно (возврат). 
                                            Дисконтная программа распространяется при заказах через почту.
                                        </p>
                                    </div>
                                </div>
                                
                            </div>
                        </div>
                        
                        <div class="col-sm-12 col-md-4">
                            <div class="card shadow-1-strong mb-3">
                                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                                    <h5 class="mb-3 mt-3 text-center text-dark"><strong>Курьер</strong></h5>
                                    <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/delivery/truck.jpg')"></div>
                                    <div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div>
                                    <div class="card-body">
                                        <p class="small text-justify">
                                            Стоимость доставки зависит от логистики и объема заказа и уточняется при подтверждении заказа.
                                            Оплата наличными курьеру при доставке.
                                            Дисконтная программа распространяется при заказах.
                                        </>
                                    </div>
                                </div>
                                
                            </div>
                        </div>
                        
                    </div>
                    
                    <div class="row">
                        
                        <div class="col-sm-12 col-md-4">
                            <div class="card shadow-1-strong mb-3">
                                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                                    <h5 class="mb-3 mt-3 text-center text-dark"><strong>Самовывоз</strong></h5>
                                    <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/delivery/selfpickup.jpg')"></div>
                                    <div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div>
                                    
                                </div>
                                <div class="card-body">
                                    <small class="mb-0 text-dark">Бесплатно, из магазина, 
                                        <a class="agr-link" href="/shops">расположенного в г.Дрогичине или г.Бресте</a></small>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                    
                </section>
                
            </div>
        </main>
    </jsp:body>
    
</t:app>
