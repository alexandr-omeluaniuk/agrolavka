<%-- 
    Document   : product-card
    Created on : Mar 14, 2021, 4:25:15 PM
    Author     : alex
--%>

<%@tag import="org.json.JSONObject"%>
<%@tag import="org.json.JSONArray"%>
<%@tag import="java.text.SimpleDateFormat"%>
<%@tag import="java.util.Objects"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="Product card" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.*,ss.agrolavka.util.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%@attribute name="cart" required="true" type="Order"%>
<%@attribute name="showCreatedDate" required="false" type="Boolean"%>
<%@attribute name="showGroup" required="false" type="Boolean"%>

<x-agr-product-card 
    data-id="${product.id}"
    data-discount="${product.discount != null && product.discount.discount != null ? product.discount.discount : ""}"
    data-in-cart="<%= CartUtils.inCart(product, cart.getPositions()) ? "true" : "" %>"
    data-in-cart-variants="<%= CartUtils.inCartVariants(product, cart.getPositions()) %>"
    data-in-stock="${product.quantity > 0 ? "true" : ""}"
    data-hide-ribbon="<%= AppCache.isBelongsToGroup("Средства защиты растений (СЗР)", product.getGroup()) ? "true" : "" %>"
    data-name="<%= product.getName().replace("\"", "'") %>"
    data-group-name="<%= showGroup != null && showGroup ? product.getGroup().getName().replace("\"", "'") : "" %>"
    data-group-link="<%= showGroup != null && showGroup ? UrlProducer.buildProductGroupUrl(product.getGroup()) : "" %>"
    data-price="${product.price}"
    data-image="${product.images.size() > 0 ? product.images.get(0).fileNameOnDisk : ""}"
    data-image-created="${product.images.size() > 0 ? product.images.get(0).createdDate : ""}"
    data-created="<%= Boolean.TRUE.equals(showCreatedDate) ? new SimpleDateFormat("dd.MM.yyyy").format(product.getCreatedDate()) : "" %>"
    data-volume="<%= product.getVolumes() != null ? product.getVolumes().replace("\"", "'") : "" %>"
    data-variants="<%= product.getVariants().toString().replace("\"", "'") %>"
    data-link="<%= UrlProducer.buildProductUrl(product)%>">
    <t:card-placeholder isProduct="true"></t:card-placeholder>
</x-agr-product-card>
