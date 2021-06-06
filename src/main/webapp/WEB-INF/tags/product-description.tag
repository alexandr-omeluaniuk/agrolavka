<%-- 
    Document   : product-description
    Created on : Jun 6, 2021, 10:34:08 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" type="ss.entity.agrolavka.Product" required="true"%>

<%-- any content can be specified here e.g.: --%>
<%
    if (product.getHtmlDescription() != null && !product.getHtmlDescription().isBlank()) {
        out.print(product.getHtmlDescription());
    } else {
%>
<h4>${product.name}</h4>
<p class="text-justify" style="white-space: pre-line;">${product.description}</p>
<%
    }
%>