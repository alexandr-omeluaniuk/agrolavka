<%-- 
    Document   : product-card
    Created on : Mar 14, 2021, 4:25:15 PM
    Author     : alex
--%>

<%@tag import="java.util.Objects"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.*,ss.agrolavka.util.UrlProducer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="group" required="true" type="ProductsGroup"%>

<%-- any content can be specified here e.g.: --%>
<div class="card">
    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
        <div class="card-img-top agr-card-image" style="background-image: url('/api/agrolavka/public/products-group-image/${group.id}')"></div>
        <a href="<%= UrlProducer.buildProductGroupUrl(group)%>">
            <div class="mask" style="background-color: rgba(251, 251, 251, 0.15)"></div>
        </a>
    </div>
    <div class="card-body">
        <h5 class="card-title">${group.name}</h5>
    </div>
</div>
