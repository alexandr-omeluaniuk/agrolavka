<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@tag import="java.util.Objects"%>
<%@tag import="ss.entity.agrolavka.Order"%>
<%@tag import="ss.entity.agrolavka.OrderPosition"%>
<%@tag import="ss.entity.agrolavka.Product"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="Product actions" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%@attribute name="cart" required="true" type="Order"%>
<%@attribute name="buttonClass" required="false" type="String"%>
<%-- any content can be specified here e.g.: --%>

<%
    boolean inCart = false;
    for (OrderPosition pos : cart.getPositions()) {
        if (Objects.equals(product.getId(), pos.getProductId())) {
            inCart = true;
            break;
        }
    }
%>

<%
    final String volumes = product.getVolumes().replace("\"", "'");
    if (product.getVolumes() != null) {
%>
    <t:product-volumes product="${product}" buttonClass="${buttonClass}"></t:product-volumes>
<%
    }
%>
<button class="btn btn-outline-info btn-rounded w-100 mt-1 ${buttonClass}" data-product-id="${product.id}" data-order="" data-volumes="<%=volumes%>">
    <i class="far fa-hand-point-up me-2"></i> Заказать сразу
</button>

<%
    if (!inCart) {
%>
<button class="btn btn-outline-success btn-rounded w-100 mt-1 ${buttonClass}" data-product-id="${product.id}" data-add="" data-volumes="<%=volumes%>">
    <i class="fas fa-cart-plus me-2"></i> В корзину
</button>
<%
} else {
%>
<button class="btn btn-outline-danger btn-rounded w-100 mt-1 ${buttonClass}" data-product-id="${product.id}" data-remove="">
    <i class="fas fa-minus-circle me-2"></i> Из корзины
</button>
<%
    }
%>