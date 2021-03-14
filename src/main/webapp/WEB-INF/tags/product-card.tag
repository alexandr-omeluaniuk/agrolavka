<%-- 
    Document   : product-card
    Created on : Mar 14, 2021, 4:25:15 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.entity.agrolavka.Product, java.net.URLEncoder"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>

<%-- any content can be specified here e.g.: --%>
<a href="/product/${product.getId()}?name=<%= URLEncoder.encode(product.getName(), "UTF-8") %>">
    <div class="card shadow-sm mb-5 bg-body rounded product-card">
        <div class="card-img-top product-image" style="background-image: url('/api/agrolavka/public/product-image/${product.id}')"></div>
        <div class="card-body">
            <h6 class="card-title text-dark" style="min-height: 60px">${product.name}</h6>
            <div class="d-flex justify-content-between align-items-center">
                <span class="card-subtitle text-muted fs-6">Цена</span>
                <span class="text-dark fw-bold product-price"><%= String.format("%.2f", product.getPrice()) %> BYN</span>
            </div>
        </div>
    </div>
</a>
