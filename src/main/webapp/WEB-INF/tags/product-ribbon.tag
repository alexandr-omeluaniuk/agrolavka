<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@tag import="ss.entity.agrolavka.Product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Product price" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%-- any content can be specified here e.g.: --%>

<div class="ribbon ribbon-top-left">
    <span class="${product.quantity > 0 ? 'bg-success' : 'bg-danger'}">
        <small>${product.quantity > 0 ? 'в наличии' : 'под заказ'}</small>
    </span>
</div>