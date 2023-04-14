<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@tag import="java.util.Map"%>
<%@tag description="Cart" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="position" required="true" type="ss.entity.agrolavka.OrderPosition"%>
<%-- any content can be specified here e.g.: --%>
<table class="table table-sm mb-0">
    <thead>
        <tr>
            <th scope="col"></th>
            <th scope="col"></th>
            <th scope="col" width="110"></th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <th scope="row text-muted">Цена</th>
            <td class="text-right">
                <c:if test="${not empty position.product.discount}">
                    <i class="fas fa-fire me-2 text-danger"></i>
                </c:if>
                <t:price priceDouble="${position.price}"></t:price>
            </td>
            <td class="text-left">
                <small class="text-muted fw-bold">
                    ${position.getQuantityLabel()}
                </small>
            </td>
        </tr>
        <tr>
            <th scope="row text-muted">Сумма</th>
            <td class="text-right">
                <t:price priceDouble="${position.price * position.quantity}"></t:price>
            </td>
            <td class="text-left">
                <small class="text-muted fw-bold">за ${position.quantity} ед.</small>
            </td>
        </tr>
    </tbody>
</table>
