<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="Top categories" pageEncoding="UTF-8"
       import="ss.agrolavka.util.AppCache,ss.entity.agrolavka.ProductsGroup,java.util.*"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="topCategories" required="true" type="java.util.List<ProductsGroup>"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<section class="text-center">
    <h4 class="mb-3"><strong>Наша основная специализация</strong></h4>
    
    <div class="agr-top-categories-swiper swiper p-3">
        <div class="swiper-wrapper">
            <c:forEach items="${topCategories}" var="category">
                <div class="swiper-slide">
                    <t:card-category group="${category}"/>
                </div>
            </c:forEach>
        </div>
        
        <div class="swiper-pagination" style="bottom: 0px;"></div>
    </div>
    
    <h6 class="mb-2 mt-4"><strong>а также множество других товаров для сада и огорода</strong></h6>
    <a class="btn btn-outline-dark btn-lg m-2" href="/catalog" role="button" >Перейти в каталог товаров</a>
    
    <script>
        (function() {
            const swiperPI = new Swiper('.agr-top-categories-swiper', {
                loop: false,
                slidesPerView: 2,
                spaceBetween: 25,
                grabCursor: true,
                autoplay: {
                    delay: 2500,
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