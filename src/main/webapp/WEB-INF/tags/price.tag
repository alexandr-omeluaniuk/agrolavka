<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag import="java.util.Map"%>
<%@tag description="Cart" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="priceDouble" required="true" type="Double"%>
<%-- any content can be specified here e.g.: --%>
<span class="mt-4 fw-bold text-dark">
    <%
        String price = String.format("%.2f", priceDouble);
        String[] parts = price.split("\\.");
        out.print(parts[0] + ".");
        out.print("<small>" + parts[1] + "</small>");
    %>
</span>