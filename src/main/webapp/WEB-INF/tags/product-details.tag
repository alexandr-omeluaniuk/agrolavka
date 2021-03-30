<%-- 
    Document   : product-details
    Created on : Feb 23, 2021, 8:45:36 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="ss.entity.agrolavka.Product"%>

<%-- any content can be specified here e.g.: --%>
<article>
    <div class="row">
        <div class="col-sm-12">
            <div class="card shadow-sm mb-2 me-4 bg-body rounded float-start">
                <div class="ribbon ribbon-top-left">
                    <span class="${product.quantity > 0 ? 'bg-success' : 'bg-danger'}">
                        <small>${product.quantity > 0 ? 'в наличии' : 'под заказ'}</small>
                    </span>
                </div>
                <div class="card-img-top agr-product-image-details" 
                     style="background-image: url('/api/agrolavka/public/product-image/${product.getId()}')"></div>
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <small>Цена</small>
                        <span class="text-dark fw-bold">
                        <%
                            String price = String.format("%.2f", product.getPrice());
                            String[] parts = price.split("\\.");
                            out.print(parts[0] + ".");
                            out.print("<small>" + parts[1] + "</small>");
                        %> <small class="text-muted">BYN</small></span>
                    </div>
                </div>
            </div>
            <h4>${product.name}</h4>
            <p class="product-text">${product.description}</p>
        </div>
    </div>
</article>