<%-- 
    Document   : product-list-item
    Created on : Mar 14, 2021, 5:15:35 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.entity.agrolavka.Product,ss.agrolavka.util.UrlProducer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>

<%-- any content can be specified here e.g.: --%>
<a href="<%= UrlProducer.buildProductUrl(product) %>">
    <div class="product-item border mb-2 p-2">
        <img src="/api/agrolavka/public/product-image/${product.id}" style="max-width: 60px; object-fit: contain;" class="me-2"></img>
        <div style="flex: 1">
            <h6 class="text-dark">${product.name}</h6>
            <small class="text-muted text-nowrap product-description">${product.description}</small>
        </div>
        <div>
            <span class="text-dark fw-bold"><%= String.format("%.2f", product.getPrice()) %> BYN</span>
        </div>
    </div>
</a>