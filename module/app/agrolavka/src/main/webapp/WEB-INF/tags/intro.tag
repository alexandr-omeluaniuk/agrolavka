<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@tag import="java.util.List"%>
<%@tag import="ss.entity.agrolavka.Slide"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Product highlights" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="visible" required="true" type="Boolean"%>
<%@attribute name="slides" required="true" type="List<Slide>"%>
<%-- any content can be specified here e.g.: --%>
<c:if test="${visible}">
    <!-- Carousel wrapper -->
    <div id="introCarousel" class="carousel slide carousel-fade shadow-2-strong" data-mdb-ride="carousel">
        <!-- Indicators -->
        <ol class="carousel-indicators">
            <c:forEach items="${slides}" var="slide" varStatus="loop">
                <li data-mdb-target="#introCarousel" data-mdb-slide-to="${loop.index}" class="${loop.index == 0 ? "active" : ""}"></li>
            </c:forEach>
            <li data-mdb-target="#introCarousel" data-mdb-slide-to="${slides.size() + 0}" class="${slides.size() == 0 ? "active" : ""}" aria-current="true"></li>
            <li data-mdb-target="#introCarousel" data-mdb-slide-to="${slides.size() + 1}" class=""></li>
            <li data-mdb-target="#introCarousel" data-mdb-slide-to="${slides.size() + 2}" class=""></li>
        </ol>

        <!-- Inner -->
        <div class="carousel-inner">
            
            <c:forEach items="${slides}" var="slide" varStatus="loop">
                <div class="carousel-item ${loop.index == 0 ? "active" : ""}" style="background-image: url('/media/${slide.getImages().get(0).getFileNameOnDisk()}')">
                    <div class="mask" style="background-color: rgba(0, 0, 0, 0.3);">
                        <div class="d-flex justify-content-center align-items-center h-100">
                            <div class="text-white text-center">
                                <h1>${slide.title}</h1>
                                <h5>${slide.subtitle}</h5>
                                <c:if test="${not empty slide.buttonText}">
                                    <a class="btn btn-outline-light btn-lg m-2" href="${slide.buttonLink}" role="button">${slide.buttonText}</a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
            
            <!-- Single item -->
            <div class="carousel-item ${slides.size() > 0 ? "" : "active"}" style="background-image: url('/assets/img/intro/catalog.webp');">
                <div class="mask" style="background-color: rgba(0, 0, 0, 0.5);">
                    <div class="d-flex justify-content-center align-items-center h-100">
                        <div class="text-white text-center">
                            <h1>Более <span class="purecounter fw-bold" data-purecounter-end="${productsCount}"></span> наименований, приглашаем за покупками!</h1>
                            <h5>выгодные и всегда актуальные цены, акции и скидки</h5>
                            <a class="btn btn-outline-light btn-lg m-2" href="/catalog" role="button" >Перейти в каталог товаров</a>
                            <a class="btn btn-outline-light btn-lg m-2" href="/shops" role="button">Наши магазины</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Single item -->
            <div class="carousel-item" style="background-image: url('/assets/img/intro/discount.webp');">
                <div class="mask" style="background-color: rgba(0, 0, 0, 0.3);">
                    <div class="d-flex justify-content-center align-items-center h-100">
                        <div class="text-white text-center">
                            <h1>Воспользуйтесь нашей дисконтной программой</h1>
                            <h5>скидки от 3% до 7% на все товары, кроме акционных</h5>
                            <a class="btn btn-outline-light btn-lg m-2" href="/discount" role="button">Условия предоставления скидок</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Single item -->
            <div class="carousel-item" style="background-image: url('/assets/img/intro/delivery.webp');">
                <div class="mask" style="background-color: rgba(0, 0, 0, 0.4);">
                    <div class="d-flex justify-content-center align-items-center h-100">
                        <div class="text-white text-center">
                            <h1>Доставка в кратчайшие сроки по всей Беларуси</h1>
                            <h5>курьерская доставка или пересылка почтой</h5>
                            <a class="btn btn-outline-light btn-lg m-2" href="/delivery" role="button">Условия доставки</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Inner -->

        <!-- Controls -->
        <a class="carousel-control-prev d-none" href="#introCarousel" role="button" data-mdb-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next d-none" href="#introCarousel" role="button" data-mdb-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
    <!-- Carousel wrapper -->
</c:if>
