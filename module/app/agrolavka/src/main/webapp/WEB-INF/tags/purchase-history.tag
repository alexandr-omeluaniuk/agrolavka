<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="Purchase history" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="purchaseHistory" required="true" type="java.util.List<ss.entity.agrolavka.Order>"%>
<%@attribute name="cart" required="true" type="ss.entity.agrolavka.Order"%>

<%-- any content can be specified here e.g.: --%>
<hr/>
<button class="btn btn-outline-dark w-100 mb-3" id="agr-switch-purchase-history">
    Показать историю заказов
</button>
<section id="agr-purchase-history-container" class="d-none">
    <c:forEach items="${purchaseHistory}" var="order">
        <div class="row">
            <div class="col-12 ps-3 d-flex justify-content-between mb-2">
                <h6 class="text-muted mb-0">Заказ от ${order.formatCreated()}</h6>
                <h6>на сумму <b class="text-dark">${order.formatTotal()} BYN</b></h6>
            </div>
            <div class="col-12 agr-purchase-history-swiper swiper w-100 mb-0 ps-3 pe-3">
                <div class="swiper-wrapper">
                    <c:forEach items="${order.positions}" var="position">
                        <c:if test="${position.product != null}">
                            <div class="swiper-slide">
                                <t:card-product product="${position.product}" cart="${cart}" showGroup="true" showCreatedDate="false"/>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
                <div class="swiper-pagination"></div>
                <div class="swiper-button-prev shadow-1-strong"></div>
                <div class="swiper-button-next shadow-1-strong"></div>
            </div>
        </div>
        <hr/>
    </c:forEach>
</section>


<script>
    (function() {
        const container = document.querySelector('#agr-purchase-history-container');
        const btn = document.querySelector('#agr-switch-purchase-history');
        btn.addEventListener('click', () => {
            if (container.classList.contains('d-none')) {
                container.classList.remove('d-none');
                btn.innerHTML = 'Скрыть историю заказов';
            } else {
                container.classList.add('d-none');
                btn.innerHTML = 'Показать историю заказов';
            }
        });

        const swiperPI = new Swiper('.agr-purchase-history-swiper', {
            loop: false,
            slidesPerView: 2,
            spaceBetween: 25,
            grabCursor: true,
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
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev'
            }
        });
    })();
</script>
