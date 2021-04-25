<%-- 
    Document   : navbar
    Created on : Feb 14, 2021, 1:58:06 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<header>
    <style>
        /* Carousel styling */
        #introCarousel,
        .carousel-inner,
        .carousel-item,
        .carousel-item.active {
            height: 100vh;
        }

        .carousel-item:nth-child(1) {
            background-image: url('https://mdbootstrap.com/img/Photos/Others/images/76.jpg');
            background-repeat: no-repeat;
            background-size: cover;
            background-position: center center;
        }
        .carousel-item:nth-child(2) {
            background-image: url('https://mdbootstrap.com/img/Photos/Others/images/77.jpg');
            background-repeat: no-repeat;
            background-size: cover;
            background-position: center center;
        }
        .carousel-item:nth-child(3) {
            background-image: url('https://mdbootstrap.com/img/Photos/Others/images/78.jpg');
            background-repeat: no-repeat;
            background-size: cover;
            background-position: center center;
        }

        /* Height for devices larger than 576px */
        @media (min-width: 992px) {
            #introCarousel {
                margin-top: -58.59px;
            }
            #introCarousel,
            .carousel-inner,
            .carousel-item,
            .carousel-item.active {
                height: 50vh;
            }
        }
    </style>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark d-none d-lg-block" style="z-index: 2000;">
        <div class="container-fluid">
            <!-- Navbar brand -->
            <a href="/" class="navbar-brand nav-link">
                <strong class="d-flex justify-content-center align-items-center">
                    <i class="fas fa-carrot agrolavka-logo me-2"></i>Агролавка
                </strong>
            </a>
            <button class="navbar-toggler" type="button" data-mdb-toggle="collapse" data-mdb-target="#navbarExample01" aria-controls="navbarExample01" aria-expanded="false" aria-label="Toggle navigation">
                <i class="fas fa-bars"></i>
            </button>
            <div class="collapse navbar-collapse" id="navbarExample01">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item active">
                        <a class="nav-link" aria-current="page" href="/">Главная</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/catalog">Каталог товаров</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#footer">Контакты</a>
                    </li>
                </ul>

                <ul class="navbar-nav d-flex flex-row">
                    <!-- Icons -->
                    <li class="nav-item me-3 me-lg-0">
                        <a class="nav-link" href="https://www.instagram.com/agrolavka.by" target="_blank">
                            <i class="fab fa-instagram"></i>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <!-- Navbar -->

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
                            <h1 class="mb-3">Learn Bootstrap 5 with MDB</h1>
                            <h5 class="mb-4">Best &amp; free guide of responsive web design</h5>
                            <a class="btn btn-outline-light btn-lg m-2" href="https://www.youtube.com/watch?v=c9B4TPnak1A" role="button" rel="nofollow" target="_blank">Start tutorial</a>
                            <a class="btn btn-outline-light btn-lg m-2" href="https://mdbootstrap.com/docs/standard/" target="_blank" role="button">Download MDB UI KIT</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Single item -->
            <div class="carousel-item">
                <div class="mask" style="background-color: rgba(0, 0, 0, 0.3);">
                    <div class="d-flex justify-content-center align-items-center h-100">
                        <div class="text-white text-center">
                            <h2>You can place here any content</h2>
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
</header>
