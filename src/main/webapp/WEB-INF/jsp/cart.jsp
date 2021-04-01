<%-- 
    Document   : cart
    Created on : Mar 30, 2021, 9:18:24 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<t:app title="Корзина" metaDescription="Ваши покупки в магазине Агролавка" canonical="/cart">
    
    <jsp:body>
        <main class="container">
            <h3 class="fw-bold"><i class="fas fa-cart-arrow-down me-2"></i>Корзина</h3>
            <hr/>
            <ul class="list-group mb-5">
                <c:forEach items="${cart.positions}" var="position">
                    <t:cart-item position="${position}"/>
                </c:forEach>
            </ul>
        </main>
    </jsp:body>
    
</t:app>
