<%-- 
    Document   : cart
    Created on : Mar 30, 2021, 9:18:24 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" import="ss.entity.agrolavka.*"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<t:app title="Корзина" metaDescription="Ваши покупки в магазине Агролавка" canonical="/cart">
    
    <jsp:body>
        <main class="mt-5 mb-5 pt-5 min-vh-100">
            <div class="container">
                <section>
                    <h3 class="text-center mb-4"><i class="fas fa-shopping-cart me-2"></i>Корзина</h3>
                    <c:choose>
                        <c:when test="${cart.positions.size() > 0}">
                            <c:forEach items="${cart.positions}" var="position">
                                <t:cart-item position="${position}"/>
                            </c:forEach>
                            <hr/>
                            <div class="d-flex justify-content-between mt-4 mb-4">
                                <h5 class="fw-bold ms-2">Общая сумма</h5>
                                <span class="text-dark fw-bold me-2" data-total-price>
                                    ${totalInteger}.<small>${totalDecimal}</small>
                                    <small class="text-muted">BYN</small>
                                </span>
                            </div>
                            <div class="d-flex justify-content-end mb-4 mt-4">
                                <a class="btn btn-success" href="/order">
                                    <i class="fas fa-truck me-2"></i> Оформить заказ
                                </a>
                            </div>
                        </c:when>    
                        <c:otherwise>
                            <div class="alert alert-warning" role="alert">
                                Ваша корзина пуста
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>
            </div>
        </main>
    </jsp:body>
    
</t:app>
