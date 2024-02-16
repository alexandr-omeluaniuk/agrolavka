<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag import="ss.entity.agrolavka.Product"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%-- any content can be specified here e.g.: --%>
<div class="modal fade" id="agr-photo-modal" tabindex="-1" aria-hidden="true" style="z-index: 3000">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content" style="background-color: white;">
            <div class="modal-header">
                <h5 class="modal-title">${product.name}</h5>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body d-flex align-items-center justify-content-center">
                <div class="agr-product-photo-modal-swiper swiper w-100" style="height: 70vh;">
                    <!-- Additional required wrapper -->
                    <div class="swiper-wrapper">
                        <c:forEach items="${product.images}" var="image" varStatus="loop">
                            <div class="swiper-slide agr-product-image-carousel" style="background-size: contain; background-image: url('/media/${image.fileNameOnDisk}?timestamp=${image.createdDate}')"></div>
                        </c:forEach>
                    </div>
                    <c:if test="${not empty product.images}">
                        <!-- If we need pagination -->
                        <div class="swiper-pagination" style="color: red;"></div>

                        <!-- If we need navigation buttons -->
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    (function() {
        const swiperPM = new Swiper('.agr-product-photo-modal-swiper', {
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
