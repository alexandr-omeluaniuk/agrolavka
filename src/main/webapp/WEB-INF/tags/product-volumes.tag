<%-- 
    Document   : product-description
    Created on : Jun 6, 2021, 10:34:08 PM
    Author     : alex
--%>

<%@tag import="java.util.ArrayList"%>
<%@tag import="java.util.List"%>
<%@tag import="java.util.Map"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" type="ss.entity.agrolavka.Product" required="true"%>
<%@attribute name="buttonClass" required="false" type="String"%>

<%-- any content can be specified here e.g.: --%>
<%
    final Map<Double, String> volumePrices = product.getVolumePrices();
    final List<Double> prices = new ArrayList<>(volumePrices.keySet());
    final Double minPrice = prices.get(0);
    final String minQuantity = volumePrices.get(minPrice).replace("л", "").replace(",", ".");
    final String volumesAttribute = product.getVolumes().replace("\"", "'");
    out.print("<div class=\"btn-group w-100 shadow-0 mt-1\" role=\"group\" aria-label=\"Volumes\" "
            + "data-volumes=\"" + volumesAttribute + "\""
            + "data-selected-volume-quantity=\"" + minQuantity + "\""
            + "data-selected-volume-price=\"" + minPrice + "\""
            + ">");
    for (int i = 0; i < prices.size(); i++) {
        final Double price = prices.get(i);
        String cl = i == 0 ? "btn-info" : "btn-outline-info";
        out.print("<button type=\"button\" class=\"agr-volume-btn btn " + cl + " btn-rounded "
            + (buttonClass == null ? "" : buttonClass) + "\" data-product-volume-price=\""
            + price + "\" data-product-volume-quantity=\"" + volumePrices.get(price).replace("л", "").replace(",", ".") + "\" data-mdb-color=\"dark\">");
        out.print(volumePrices.get(price));
        out.print("</button>");
    }
    out.print("</div>");
%>