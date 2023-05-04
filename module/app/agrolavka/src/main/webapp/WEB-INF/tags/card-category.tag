<%-- 
    Document   : product-card
    Created on : Mar 14, 2021, 4:25:15 PM
    Author     : alex
--%>

<%@tag import="java.util.Objects"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.*,ss.agrolavka.util.UrlProducer,ss.agrolavka.util.AppCache"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="group" required="true" type="ProductsGroup"%>

<%-- any content can be specified here e.g.: --%>
<div class="card shadow-1-strong mb-4 hover-shadow">
    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
        <c:choose>
            <c:when test="${group.images.size() > 0}">
                <div class="card-img-top agr-card-image" style="background-image: url('/media/${group.images.get(0).fileNameOnDisk}?timestamp=${group.images.get(0).createdDate}')"></div>
            </c:when>
            <c:otherwise>
                <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/no-image.png')"></div>
            </c:otherwise>
        </c:choose>
        <a href="<%= UrlProducer.buildProductGroupUrl(group)%>">
            <div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div>
        </a>
        <div class="card-body agr-card-body-category">
            <h6 class="card-title mb-0">${group.name}</h6>
        </div>
    </div>
</div>
