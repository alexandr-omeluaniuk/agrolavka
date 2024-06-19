<%-- 
    Document   : catalog
    Created on : Feb 18, 2021, 9:04:42 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="modal" tagdir="/WEB-INF/tags/modal" %>

<t:app title="${title}" metaDescription="${metaDescription}" canonical="${canonical}">
    <jsp:attribute name="headSection">
        <link rel="stylesheet" href="/assets/vendor/swiper/swiper-bundle.min.css"/>
        <script src="/assets/vendor/swiper/swiper-bundle.min.js"></script>
    </jsp:attribute>
    
    <jsp:body>
        <main class="min-vh-100">
            <div class="container mb-5">
                
                <section>
                    <h3 class="text-center mb-4">${group != null ? group.name : 'Каталог товаров'}</h3>
                    <c:choose>
                        <c:when test="${group != null}">
                            <p class="text-muted text-justify">${group.description}</p>
                        </c:when>    
                        <c:otherwise>
                            <p class="text-uppercase text-muted text-center">Товары для сада и огорода</p>
                        </c:otherwise>
                    </c:choose>
                    <t:breadcrumb label="${breadcrumbLabel}" groups="${breadcrumbPath}"/>
                    <t:categories-grid categories="${categories}" />
                </section>
                <c:if test="${empty categories}">
                    <section class="pb-4">
                        <t:products-search-result searchResult="${searchResult}" pages="${searchResultPages}"
                                                  page="${page}" view="${view}" sort="${sort}" group="${group}"
                                                  cart="${cart}">
                        </t:products-search-result>
                    </section>
                </c:if>
                <c:if test="${not empty purchaseHistoryProducts}">
                    <hr/>
                    <h6 class="text-center mb-3">Ранее заказывали</h6>
                    <div class="col-12 agr-purchase-history-products-swiper swiper w-100 mb-0 ps-3 pe-3">
                        <div class="swiper-wrapper">
                            <c:forEach items="${purchaseHistoryProducts}" var="p">
                                <div class="swiper-slide">
                                    <t:card-product product="${p}" cart="${cart}" showCreatedDate="false" showGroup="true"/>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="swiper-pagination"></div>
                        <div class="swiper-button-prev shadow-1-strong"></div>
                        <div class="swiper-button-next shadow-1-strong"></div>
                    </div>

                    <script>
                        (function() {
                            const swiperPI = new Swiper('.agr-purchase-history-products-swiper', {
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
                </c:if>
            </div>
        </main>
        <modal:one-click-order-modal/>
        <modal:add-to-cart-modal/>
    </jsp:body>

</t:app>
