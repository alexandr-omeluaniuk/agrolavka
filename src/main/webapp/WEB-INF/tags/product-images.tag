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

<div class="swiper shadow-1-strong w-100 ${product.images.size() > 1 ? "mb-5" : ""}" style="height: 360px;">
    <t:product-ribbon product="${product}"></t:product-ribbon>
    <!-- Additional required wrapper -->
    <div class="swiper-wrapper">
        <c:forEach items="${product.images}" var="image" varStatus="loop">
            <div class="swiper-slide agr-product-image-carousel" style="background-image: url('/media/${image.fileNameOnDisk}?timestamp=${image.createdDate}')"></div>
        </c:forEach>
    </div>
    <!-- If we need pagination -->
    <div class="swiper-pagination" style="color: red;"></div>

    <!-- If we need navigation buttons -->
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>

</div>

<script>
    const swiper = new Swiper('.swiper', {
        pagination: {
            el: '.swiper-pagination'
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev'
        }
    });
</script>
