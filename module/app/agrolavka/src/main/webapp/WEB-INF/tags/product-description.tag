<%-- 
    Document   : product-description
    Created on : Jun 6, 2021, 10:34:08 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" type="ss.entity.agrolavka.Product" required="true"%>

<%-- any content can be specified here e.g.: --%>
<%
    if (product.getHtmlDescription() != null && !product.getHtmlDescription().isBlank()) {
        out.print(product.getHtmlDescription());
    } else {
%>
<h4>${product.name}</h4>
<c:if test="${not empty product.videoURL}">
    <iframe src="${product.videoURL}" width="100%"  height="350"
            title="YouTube video player" frameborder="0" 
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>

    </iframe>
</c:if>
<p class="text-justify" style="white-space: pre-line;">${product.description}</p>
<%
    }
%>