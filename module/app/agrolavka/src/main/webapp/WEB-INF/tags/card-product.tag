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

<x-agr-product-card 
    data-id="${product.id}"
    data-discount="${product.discount != null && product.discount.discount != null ? product.discount.discount : ""}"
    data-in-cart="<%= cart.getPositions().stream().filter(pos -> Objects.equals(product.getId(), pos.getProductId())).findFirst().isPresent() ? "true" : "" %>"
    data-in-stock="${product.quantity > 0 ? "true" : ""}"
    data-hide-ribbon="<%= AppCache.isBelongsToGroup("Средства защиты растений (СЗР)", product.getGroup()) ? "true" : "" %>"
    data-name="<%= product.getName().replace("\"", "'") %>"
    data-price="${product.price}"
    data-image="${product.images.size() > 0 ? product.images.get(0).fileNameOnDisk : ""}"
    data-image-created="${product.images.size() > 0 ? product.images.get(0).createdDate : ""}"
    data-created="<%= Boolean.TRUE.equals(showCreatedDate) ? new SimpleDateFormat("dd.MM.yyyy").format(product.getCreatedDate()) : "" %>"
    data-volume="<%= product.getVolumes() != null ? product.getVolumes().replace("\"", "'") : "" %>"
    data-link="<%= UrlProducer.buildProductUrl(product)%>">
        <div class="card shadow-1-strong mb-4" aria-hidden="true">
            <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/no-image.png')"></div>
                <div class="card-body agr-card-body-category">
                    <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
                    <a href="#" tabindex="-1" class="btn disabled placeholder col-5 bg-dark"></a>
                    <a href="#" tabindex="-1" class="btn disabled placeholder col-5 bg-dark"></a>
                    <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
                    <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
                    <a href="#" tabindex="-1" class="btn disabled placeholder col-5 bg-dark"></a>
                    <a href="#" tabindex="-1" class="btn disabled placeholder col-5 bg-dark"></a>
                    <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
                </div>
            </div>
        </div>
</x-agr-product-card>
