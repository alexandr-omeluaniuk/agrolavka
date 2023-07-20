<%-- 
    Document   : discounts-highlight
    Created on : Sep 2, 2021, 7:57:26 PM
    Author     : alex
--%>

<%@tag description="discount highlight" pageEncoding="UTF-8" import="ss.entity.agrolavka.*, java.util.*"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="products" required="true" type="List<Product>"%>

<%-- any content can be specified here e.g.: --%>
<section class="text-center">
    <h4 class="mb-3"><strong>Акции</strong></h4>
    <h6 class="mb-2">Покупайте товары дешевле чем обычно!</h6>
    
    <div class="agr-products-with-discounts-swiper swiper p-2">
        <div class="swiper-wrapper">
            <c:forEach items="${products}" var="product">
                <div class="swiper-slide">
                    <t:card-product product="${product}" cart="${cart}"/>
                </div>
            </c:forEach>
        </div>
        
        <div class="swiper-pagination" style="bottom: 0px;"></div>
    </div>
    
    <h6 class="mb-2 mt-4"><strong>нажмите, чтобы просмотреть полный список акционных товаров</strong></h6>
    <a class="btn btn-outline-danger btn-lg m-2" href="/promotions" role="button" ><i class="fas fa-fire me-1"></i> Все товары на акции</a>
    
    <script>
        (function() {
            const swiperPI = new Swiper('.agr-products-with-discounts-swiper', {
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