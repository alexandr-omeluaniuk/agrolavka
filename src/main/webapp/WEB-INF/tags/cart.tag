<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Cart" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="cart" required="true" type="ss.entity.agrolavka.Order"%>
<%@attribute name="totalInteger" required="true" type="String"%>
<%@attribute name="totalDecimal" required="true" type="String"%>
<%-- any content can be specified here e.g.: --%>
<a href="/cart" class="btn btn-outline-light d-flex align-items-center">
    <i class="fas fa-shopping-cart" style="position: relative;">
        <span class="badge rounded-pill agr-cart-badge">${cart.positions.size()}</span>
    </i>
    <span class="text-light fw-bold ms-2" data-cart-price>
        ${totalInteger}.<small>${totalDecimal}</small>
    </span>
</a>
