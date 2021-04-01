<%-- 
    Document   : cart-item
    Created on : Apr 1, 2021, 8:58:45 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.entity.agrolavka.*,ss.agrolavka.util.UrlProducer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="position" required="true" type="OrderPosition"%>

<%-- any content can be specified here e.g.: --%>
<li class="list-group-item">
    <div class="d-flex">
        <div>
            <img src="/api/agrolavka/public/product-image/${position.product.id}" alt="${position.product.name}" width="100px"/>
        </div>
        <div style="flex: 1;" class="ps-3 pe-3">
            <a href="<%= UrlProducer.buildProductUrl(position.getProduct()) %>"><h6>${position.product.name}</h6></a>
            <small class="text-muted agr-cart-position-description">
                ${position.product.description}
            </small>
        </div>
        <div>
            TODO: buttons
        </div>
    </div>
</li>