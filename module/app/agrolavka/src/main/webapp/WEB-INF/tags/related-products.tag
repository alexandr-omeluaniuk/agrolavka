<%-- 
    Document   : discounts-highlight
    Created on : Sep 2, 2021, 7:57:26 PM
    Author     : alex
--%>

<%@tag description="related products" pageEncoding="UTF-8" import="ss.entity.agrolavka.*, java.util.*"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="products" required="true" type="List<Product>"%>

<%-- any content can be specified here e.g.: --%>
<section class="text-center mb-5">
    <h6 class="mb-3">Чаще всего с этим товаром покупают</h6>
    
    <div class="agr-related-products-swiper swiper p-2">
        <div class="swiper-wrapper">
            <c:forEach items="${products}" var="product">
                <div class="swiper-slide">
                    <t:card-product product="${product}" cart="${cart}" showGroup="true"/>
                </div>
            </c:forEach>
        </div>
        
        <div class="swiper-pagination" style="bottom: 0px;"></div>
    </div>

    <script>
        (function() {
            const swiperPI = new Swiper('.agr-related-products-swiper', {
                loop: false,
                slidesPerView: 2,
                spaceBetween: 25,
                grabCursor: true,
                autoplay: {
                    delay: 10000,
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