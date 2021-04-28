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
<%@attribute name="product" required="true" type="Product"%>
<%@attribute name="cart" required="true" type="Order"%>

<%-- any content can be specified here e.g.: --%>
<div class="card shadow-1-strong mb-4">
    <div class="ribbon ribbon-top-left">
        <span class="${product.quantity > 0 ? 'bg-success' : 'bg-danger'}">
            <small>${product.quantity > 0 ? 'в наличии' : 'под заказ'}</small>
        </span>
    </div>
    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
        <div class="card-img-top agr-card-image" style="background-image: url('/api/agrolavka/public/product-image/${product.id}')"></div>
        <a href="<%= UrlProducer.buildProductUrl(product) %>">
            <div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div>
        </a>
        <div class="card-body" style="min-height: 100px;">
            <h6 class="card-title text-dark" style="min-height: 60px;">${product.name}</h6>
            <div class="d-flex justify-content-between align-items-center">
                <span class="card-subtitle text-muted fs-6">Цена</span>
                <span class="text-dark fw-bold">
                    <%
                        String price = String.format("%.2f", product.getPrice());
                        String[] parts = price.split("\\.");
                        out.print(parts[0] + ".");
                        out.print("<small>" + parts[1] + "</small>");
                    %> <small class="text-muted">BYN</small></span>
            </div>
            <%
                boolean inCart = false;
                for (OrderPosition pos : cart.getPositions()) {
                    if (Objects.equals(product.getId(), pos.getProductId())) {
                        inCart = true;
                        break;
                    }
                }
            %>
            <%
                if (!inCart) {
            %>
            <button class="btn btn-sm btn-success w-100 mt-1" data-product-id="${product.id}" data-add="" style="z-index: 9000;">
                <i class="fas fa-cart-plus me-2"></i> В корзину
            </button>
            <%
                } else {
            %>
            <button class="btn btn-sm btn-danger w-100 mt-1" data-product-id="${product.id}" data-remove="" style="z-index: 9000;">
                <i class="fas fa-minus-circle me-2"></i> Из корзины
            </button>
            <%
                }
            %>
        </div>
    </div>
</div>
