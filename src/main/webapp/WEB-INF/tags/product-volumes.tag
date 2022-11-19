<%-- 
    Document   : product-description
    Created on : Jun 6, 2021, 10:34:08 PM
    Author     : alex
--%>

<%@tag import="org.json.JSONObject"%>
<%@tag import="org.json.JSONArray"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" type="ss.entity.agrolavka.Product" required="true"%>

<%-- any content can be specified here e.g.: --%>
<%
    JSONArray array = new JSONArray(product.getVolumes());
    out.print("<div class=\"btn-group w-100 shadow-0 mt-1\" role=\"group\" aria-label=\"Volumes\">");
    for (int i = 0; i < array.length(); i++) {
        JSONObject obj = array.getJSONObject(i);
        String cl = i == 0 ? "btn-info" : "btn-outline-info";
        out.print("<button type=\"button\" class=\"agr-volume-btn btn " + cl + " btn-rounded agr-card-button\" data-product-volume-price=\""
            + obj.getDouble("p") + "\" data-mdb-color=\"dark\">");
        out.print(obj.getString("v"));
        out.print("</button>");
    }
    out.print("</div>");
%>