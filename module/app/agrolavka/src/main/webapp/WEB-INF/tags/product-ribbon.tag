<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@tag import="ss.entity.agrolavka.Product"%>
<%@tag import="ss.agrolavka.util.AppCache"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Product price" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%-- any content can be specified here e.g.: --%>
<% if (product.getDiscount() == null && !AppCache.isBelongsToGroup("Средства защиты растений (СЗР)", product.getGroup())) { %>
<div class="ribbon ribbon-top-left">
    <span class="${product.quantity > 0 ? 'bg-success' : 'bg-gray'}">
        <small>${product.quantity > 0 ? 'в наличии' : 'под заказ'}</small>
    </span>
</div>
<%
    }
%>

<% if (product.getDiscount() != null) { %>
<div class="ribbon ribbon-top-left">
    <span class="bg-danger">
        <small><i class="fas fa-fire me-1"></i>акция -${String.format("%100.0f", product.getDiscount().getDiscount())}%</small>
    </span>
</div>
<% } %>