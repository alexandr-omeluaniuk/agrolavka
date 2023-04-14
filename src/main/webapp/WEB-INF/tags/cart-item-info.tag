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
                    <t:price priceDouble="${position.product.discountPrice}"></t:price>
                </c:if>
                <c:if test="${empty position.product.discount}">
                    <t:price priceDouble="${position.product.price}"></t:price>
                </c:if>
            </td>
            <td class="text-left">
                <%
                    out.print("<small class=\"text-muted fw-bold ms-1\">");
                    if (position.getProduct().getVolumes() != null) {
                        try {
                            final Map<Double, String> volumePrices = position.getProduct().getVolumePrices();
                            final String volume = volumePrices.get(position.getPrice());
                            if (volume != null) {
                                out.print("за " + volume);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        out.print("за 1 ед.");
                    }
                    out.print("</small>");
                %>
            </td>
        </tr>
        <tr>
            <th scope="row text-muted">Сумма</th>
            <td class="text-right">
                <span class="mt-4 fw-bold text-dark">
                    <%
                        Integer quantity = position.getQuantity();
                        Double subtotal = position.getPrice() * quantity;
                        String sum = String.format("%.2f", subtotal);
                        String[] parts2 = sum.split("\\.");
                        out.print(parts2[0] + ".");
                        out.print("<small>" + parts2[1] + "</small>");
                    %>
            </td>
            <td class="text-left">
                <small class="text-muted fw-bold ms-1">за ${position.quantity} ед.</small>
            </td>
        </tr>
    </tbody>
</table>
