<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@tag description="Product highlights" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<!-- Carousel wrapper -->
<div id="introCarousel" class="carousel slide carousel-fade shadow-2-strong" data-mdb-ride="carousel">
    <!-- Indicators -->
    <ol class="carousel-indicators">
        <li data-mdb-target="#introCarousel" data-mdb-slide-to="0" class="active" aria-current="true"></li>
        <li data-mdb-target="#introCarousel" data-mdb-slide-to="1" class=""></li>
        <li data-mdb-target="#introCarousel" data-mdb-slide-to="2" class=""></li>
    </ol>

    <!-- Inner -->
    <div class="carousel-inner">
        <!-- Single item -->
        <div class="carousel-item active">
            <div class="mask" style="background-color: rgba(0, 0, 0, 0.6);">
                <div class="d-flex justify-content-center align-items-center h-100">
                    <div class="text-white text-center">
                        <h1 class="mb-3">Более <span class="purecounter fw-bold" data-purecounter-end="${productsCount}"></span> наименований, приглашаем за покупками!</h1>
                        <h5 class="mb-4">выгодные цены, акции и скидки</h5>
                        <a class="btn btn-outline-light btn-lg m-2" href="/catalog" role="button" >Перейти в каталог товаров</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Single item -->
        <div class="carousel-item">
            <div class="mask" style="background-color: rgba(0, 0, 0, 0.3);">
                <div class="d-flex justify-content-center align-items-center h-100">
                    <div class="text-white text-center">
                        <h1>Воспользуйтесь нашей дисконтной программой</h1>
                        <h5>скидки от 3% до 7% на все товары, кроме акционных</h5>
                    </div>
                </div>
            </div>
        </div>

        <!-- Single item -->
        <div class="carousel-item">
            <div class="mask" style="
                 background: linear-gradient(
                 45deg,
                 rgba(29, 236, 197, 0.7),
                 rgba(91, 14, 214, 0.7) 100%
                 );
                 ">
                <div class="d-flex justify-content-center align-items-center h-100">
                    <div class="text-white text-center">
                        <h2>And cover it with any mask</h2>
                        <a class="btn btn-outline-light btn-lg m-2" href="https://mdbootstrap.com/docs/standard/content-styles/masks/" target="_blank" role="button">Learn about masks</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Inner -->

    <!-- Controls -->
    <a class="carousel-control-prev" href="#introCarousel" role="button" data-mdb-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" href="#introCarousel" role="button" data-mdb-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>
<!-- Carousel wrapper -->