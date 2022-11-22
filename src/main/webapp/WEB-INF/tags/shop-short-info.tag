<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.Shop"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="shop" required="true" type="ss.entity.agrolavka.Shop"%>

<%-- any content can be specified here e.g.: --%>
<a class="agr-shop-address-link" href="/shops"><p class="agr-shop-address">${shop.address}</p></a>
<% if (shop.getPhone() != null) { %>
<div class="d-flex justify-content-between align-items-center">
    <h6 class="mt-2">Телефон</h6>
    <a class="text-white agr-shop-phone" href="tel:${shop.phone}">${shop.phone}</a>
</div>
<% } %>
<h6 class="mt-2">Время работы</h6>
<%
    final String workingWhours = shop.getWorkingHours();
    final String[] lines = workingWhours.split("\n");
    for (final String line : lines) {
        if (line.contains("|")) {
            final String subline1 = line.substring(0, line.lastIndexOf("|"));
            final String subline2 = line.substring(line.lastIndexOf("|") + 1);
            out.print("<div class=\"d-flex justify-content-between align-items-center\">");
            out.print("<small><b>" + subline1 + "</b></small> <small>" + subline2 + "</small>");
            out.print("</div>");
        } else {
            out.print("<p style=\"white-space: pre;\">${shop.workingHours}</p>");
        }
    }
%>
<hr/>