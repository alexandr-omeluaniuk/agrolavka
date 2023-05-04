<%-- 
    Document   : product-description
    Created on : Jun 6, 2021, 10:34:08 PM
    Author     : alex
--%>

<%@tag import="ss.agrolavka.wrapper.ProductVolume"%>
<%@tag import="java.util.ArrayList"%>
<%@tag import="java.util.List"%>
<%@tag import="java.util.Map"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" type="ss.entity.agrolavka.Product" required="true"%>
<%@attribute name="buttonClass" required="false" type="String"%>

<%-- any content can be specified here e.g.: --%>
<%
    final List<ProductVolume> volumes = product.getProductVolumes();
    final Double minPrice = volumes.get(0).getPrice();
    final Double selectedQuantity = volumes.get(0).getAmount();
%>
<div class="btn-group w-100 shadow-0 mt-1" role="group" aria-label="Volumes" 
     data-volumes="<%= product.getVolumes().replace("\"", "'") %>" 
     data-selected-volume-quantity="<%= selectedQuantity %>" 
     data-selected-volume-price="<%= minPrice %>">
    <%
        for (int i = 0; i < volumes.size(); i++) {
            final ProductVolume volume = volumes.get(i);
            String cl = i == 0 ? "btn-info" : "btn-outline-info";
            out.print("<button type=\"button\" class=\"agr-volume-btn btn " + cl + " btn-rounded "
                + (buttonClass == null ? "" : buttonClass) + "\" data-product-volume-price=\""
                + volume.getPrice() + "\" data-product-volume-quantity=\"" + volume.getAmount() + "\" data-mdb-color=\"dark\">");
            out.print(volume.getVolumeLabel());
            out.print("</button>");
        }
    %>
</div>