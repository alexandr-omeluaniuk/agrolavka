<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Product highlights" pageEncoding="UTF-8" import="ss.entity.agrolavka.*"%>
<%@attribute name="newProducts" required="true" type="java.util.List<Product>"%>
<%@attribute name="cart" required="true" type="Order"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<section class="text-center">
    <h4 class="mb-3"><strong>Новинки</strong></h4>
    <h6 class="mb-2">Последнее поступление товаров</h6>
    
    <div class="agr-new-products-swiper swiper p-2">
        <div class="swiper-wrapper">
            <c:forEach items="${newProducts}" var="product">
                <div class="swiper-slide">
                    <t:card-product product="${product}" cart="${cart}" showCreatedDate="true"/>
                </div>
            </c:forEach>
        </div>
        
        <div class="swiper-pagination" style="bottom: 0px;"></div>
    </div>
        
    <script>
        (function() {
            const swiperPI = new Swiper('.agr-new-products-swiper', {
                loop: false,
                slidesPerView: 2,
                spaceBetween: 25,
                grabCursor: true,
                autoplay: {
                    delay: 4500,
                    disableOnInteraction: true
                },
                pagination: {
                    el: '.swiper-pagination'
                },
                breakpoints: {
                    768: {
                        slidesPerView: 3
                    },
                    1200: {
                        slidesPerView: 4
                    }
                }
            });
        })();
    </script>
    
</section>