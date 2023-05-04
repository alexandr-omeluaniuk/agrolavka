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
<%@attribute name="rowClass" required="false" type="String"%>
<%-- any content can be specified here e.g.: --%>

<div class="d-flex align-items-center justify-content-between ${rowClass}">
    <small class="text-muted">Цена</small>
    <span class="agr-price fw-bold ${not empty product.discount ? 'text-decoration-line-through text-muted' : 'text-dark'}">
        <%
            String price = String.format("%.2f", product.getPrice());
            String[] parts = price.split("\\.");
            out.print(parts[0] + ".");
            out.print("<small>" + parts[1] + "</small>");
        %> <small class="text-muted">BYN</small>
    </span>
</div>
<c:if test="${not empty product.discount}">
    <div class="d-flex align-items-center justify-content-between ${rowClass}">
        <small class="text-danger"><i class="fas fa-fire me-1"></i> Акция</small>
        <div class="text-danger fw-bold">
            <%
                String priceWithDiscount = String.format("%.2f", product.getDiscountPrice());
                String[] priceWithDiscountParts = priceWithDiscount.split("\\.");
                out.print(priceWithDiscountParts[0] + ".");
                out.print("<small>" + priceWithDiscountParts[1] + "</small>");
            %> <small>BYN</small>
        </div>
    </div>
</c:if>