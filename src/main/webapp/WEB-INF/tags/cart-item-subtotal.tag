<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag import="java.util.Map"%>
<%@tag description="Cart" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="position" required="true" type="ss.entity.agrolavka.OrderPosition"%>
<%-- any content can be specified here e.g.: --%>
<div class="d-flex align-items-center justify-content-between w-100">
    <div>
        <span class="fw-bold text-muted">Сумма:</span>
    </div>
    <div>
        <span class="mt-4 fw-bold text-dark">
        <%
            Double priceVal = position.getPrice();
            if (position.getProduct() != null && position.getProduct().getVolumes() == null) {
                priceVal = position.getProduct().getPrice();
            }
            Integer quantity = position.getQuantity();
            Double subtotal = priceVal * quantity;
            String sum = String.format("%.2f", subtotal);
            String[] parts = sum.split("\\.");
            out.print(parts[0] + ".");
            out.print("<small>" + parts[1] + "</small>");
        %> <small class="text-muted">BYN</small></span>
        <%
            out.print("<small class=\"text-muted fw-bold ms-1\">за ");
            out.print(position.getQuantity());
            out.print(" ед.</small>");
        %>
    </div>
</div>
