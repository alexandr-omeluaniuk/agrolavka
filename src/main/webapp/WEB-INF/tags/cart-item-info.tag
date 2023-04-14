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
                    <button class="btn btn-sm btn-danger btn-rounded fs-6 me-2">
                        <i class="fas fa-fire me-2"></i>
                        <%
                            String priceWithDiscount = String.format("%.2f", position.getProduct().getDiscountPrice());
                            String[] priceWithDiscountParts = priceWithDiscount.split("\\.");
                            out.print(priceWithDiscountParts[0] + ".");
                            out.print("<small>" + priceWithDiscountParts[1] + "</small>");
                        %> <small>BYN</small>
                    </button>
                </c:if>
                <span class="mt-4 fw-bold ${not empty position.product.discount ? 'text-decoration-line-through text-muted' : 'text-dark'}">
                    <%
                        Double priceVal = position.getPrice();
                        if (position.getProduct() != null && position.getProduct().getVolumes() == null) {
                            priceVal = position.getProduct().getPrice();
                        }
                        String price = String.format("%.2f", priceVal);
                        String[] parts = price.split("\\.");
                        out.print(parts[0] + ".");
                        out.print("<small>" + parts[1] + "</small>");
                    %>
            </td>
            <td class="text-right">
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
                        Double subtotal = priceVal * quantity;
                        String sum = String.format("%.2f", subtotal);
                        String[] parts2 = sum.split("\\.");
                        out.print(parts2[0] + ".");
                        out.print("<small>" + parts2[1] + "</small>");
                    %>
            </td>
            <td class="text-right">
                <small class="text-muted fw-bold ms-1">за ${position.quantity} ед.</small>
            </td>
        </tr>
    </tbody>
</table>
