<%-- 
    Document   : welcome
    Created on : Feb 14, 2021, 1:19:43 PM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="Агролавка | Главная" activePage="HOME">
    <section id="hero" class="clearfix">
        <div class="container d-flex h-100">
            <div class="row justify-content-center" data-aos="fade-up" style="width: 100%">
                <div class="col-lg-8 intro-info order-lg-first order-last" data-aos="zoom-in" data-aos-delay="100">
                    <h2>Rapid Solutions<br>for Your <span>Business!</span></h2>
                    <div>
                        <a href="#about" class="btn-get-started scrollto">Get Started</a>
                    </div>
                </div>

                <div id="highlights" class="col-lg-4 intro-info order-lg-last order-first" data-aos="zoom-in" data-aos-delay="100">

                    <header class="section-header">
                        <h5 class="text-center">Большое поступление семян овощных и цветочных культур, элитные голландские обработанные семена!</h5>
                    </header>

                    <div class="row justify-content-center">
                        <div class="col-lg-12">
                            <div id="carouselExampleCaptions" class="carousel slide" data-bs-ride="carousel">
                                <div class="carousel-indicators">
                                    <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
                                    <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1" aria-label="Slide 2"></button>
                                    <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2" aria-label="Slide 3"></button>
                                    <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="3" aria-label="Slide 4"></button>
                                    <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="4" aria-label="Slide 5"></button>
                                </div>
                                <div class="carousel-inner">
                                    <div class="carousel-item active">
                                        <img src="/assets/img/highlights/1.jpg" class="img-fluid img-thumbnail" alt="Семена овощных культур">
                                        <div class="carousel-caption d-none d-md-block">
                                            <h5>Семена овощных культур</h5>
                                            <p>Морковка</p>
                                        </div>
                                    </div>
                                    <div class="carousel-item">
                                        <img src="/assets/img/highlights/2.jpg" class="img-thumbnail" alt="Семена овощных культур">
                                        <div class="carousel-caption d-none d-md-block">
                                            <h5>Семена овощных культур</h5>
                                            <p>Огурцы, кукуруза</p>
                                        </div>
                                    </div>
                                    <div class="carousel-item">
                                        <img src="/assets/img/highlights/3.jpg" class="img-thumbnail" alt="Лук-севок">
                                        <div class="carousel-caption d-none d-md-block">
                                            <h5>Лук-севок</h5>
                                            <p>Элитный голландский сорт</p>
                                        </div>
                                    </div>
                                    <div class="carousel-item">
                                        <img src="/assets/img/highlights/4.jpg" class="img-thumbnail" alt="Удобрения">
                                        <div class="carousel-caption d-none d-md-block">
                                            <h5>Аксессуары и удобрения</h5>
                                            <p>Горшки,ящики,пакеты для рассады</p>
                                        </div>
                                    </div>
                                    <div class="carousel-item">
                                        <img src="/assets/img/highlights/5.jpg" class="img-thumbnail" alt="Аксессуары">
                                        <div class="carousel-caption d-none d-md-block">
                                            <h5>Семена овощных культур</h5>
                                            <p>Перец, помидоры</p>
                                        </div>
                                    </div>
                                </div>
                                <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions"  data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions"  data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
    </section>
</t:app>