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
<x-agr-category-card 
    data-name="${group.name}" 
    data-image="${group.images.size() > 0 ? group.images.get(0).fileNameOnDisk : ""}"
    data-image-created="${group.images.size() > 0 ? group.images.get(0).createdDate : ""}"
    data-link="<%= UrlProducer.buildProductGroupUrl(group)%>"></x-agr-category-card>
