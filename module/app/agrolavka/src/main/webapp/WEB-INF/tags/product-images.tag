<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@tag import="ss.entity.agrolavka.Product,ss.agrolavka.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="Product images" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%-- any content can be specified here e.g.: --%>

<div class="agr-product-photo-swiper swiper shadow-1-strong w-100 mb-2" style="height: 360px;">
    <x-agr-product-ribbon 
        data-discount="${product.discount != null && product.discount.discount != null ? product.discount.discount : ""}"
        data-hide="<%= AppCache.isBelongsToGroup("Средства защиты растений (СЗР)", product.getGroup()) ? "true" : "" %>"
        data-in-stock="${product.quantity > 0 ? "true" : ""}"></x-agr-product-ribbon>
    <div class="swiper-wrapper">
        <c:forEach items="${product.images}" var="image" varStatus="loop">
            <div class="swiper-slide agr-product-image-carousel" style="background-image: url('/media/${image.fileNameOnDisk}?timestamp=${image.createdDate}')"></div>
        </c:forEach>
        <c:if test="${empty product.images}">
            <div class="swiper-slide" style="background-image: url('/assets/img/no-image.png'); background-repeat: no-repeat; background-size: contain;"></div>
        </c:if>
    </div>
    <c:if test="${not empty product.images}">
        <div class="swiper-pagination"></div>
        <div class="swiper-button-prev shadow-1-strong"></div>
        <div class="swiper-button-next shadow-1-strong"></div>
    </c:if>
</div>

<script>
    (function() {
        const swiperPI = new Swiper('.agr-product-photo-swiper', {
            loop: false,
            pagination: {
                el: '.swiper-pagination'
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev'
            }
        });
    })();
</script>
