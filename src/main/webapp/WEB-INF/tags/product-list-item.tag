<%-- 
    Document   : product-list-item
    Created on : Mar 14, 2021, 5:15:35 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.entity.agrolavka.Product,ss.agrolavka.util.UrlProducer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>

<%-- any content can be specified here e.g.: --%>
<a href="<%= UrlProducer.buildProductUrl(product)%>" class="list-group-item list-group-item-action">
    <div class="d-flex w-100 justify-content-between">
        <h6 class="mb-1">
            <c:choose>
                <c:when test="${product.hasImages}">
                    <img src="/media/${product.images.get(0).fileNameOnDisk}" style="width: 40px; height: 40px; object-fit: contain;" class="me-2"></img>
                </c:when>
                <c:otherwise>
                    <img src="/assets/img/no-image.png" style="width: 40px; height: 40px; object-fit: contain;" class="me-2"></img>
                </c:otherwise>
            </c:choose>
            
            ${product.name}</h6>
        <small class="text-dark fw-bold text-right"><%= String.format("%.2f", product.getPrice())%> BYN</small>
    </div>
</a>