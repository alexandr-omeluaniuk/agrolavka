<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@tag import="ss.entity.agrolavka.Product"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="Product images" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%-- any content can be specified here e.g.: --%>

<!-- Carousel wrapper -->
<div id="productImagesCarousel" class="carousel slide shadow-1-strong w-100 ${product.images.size() > 1 ? "mb-5" : ""}" 
     mdb-data-touch="true" data-mdb-ride="carousel">
    <!-- Indicators -->
    <c:if test="${product.images.size() > 1}">
        <ol class="carousel-indicators">
            <c:forEach items="${product.images}" var="slide" varStatus="loop">
                <li data-mdb-target="#productImagesCarousel" data-mdb-slide-to="${loop.index}" class="${loop.index == 0 ? "active" : ""}"></li>
            </c:forEach>
        </ol>
    </c:if>

    <!-- Inner -->
    <div class="carousel-inner">

        <c:forEach items="${product.images}" var="image" varStatus="loop">
            <div class="carousel-item agr-product-image-carousel ${loop.index == 0 ? "active" : ""}"
                 data-mdb-interval="36000"
                 style="background-image: url('/media/${image.fileNameOnDisk}?timestamp=${image.createdDate}')">
                <t:product-ribbon product="${product}"></t:product-ribbon>
            </div>
        </c:forEach>
        
        <c:if test="${product.images.size() == 0}">
            <div class="carousel-item agr-product-image-carousel active" style="background-image: url('/assets/img/no-image.png')">

            </div>
        </c:if>

    </div>
    <!-- Inner -->

    <!-- Controls -->
    <c:if test="${product.images.size() > 1}">
        <button class="carousel-control-prev" type="button" data-mdb-target="#productImagesCarousel" data-mdb-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-mdb-target="#productImagesCarousel" data-mdb-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </c:if>
</div>
<!-- Carousel wrapper -->