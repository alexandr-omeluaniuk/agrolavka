<%-- 
    Document   : cart-item
    Created on : Apr 1, 2021, 8:58:45 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.entity.agrolavka.*,ss.agrolavka.util.UrlProducer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="position" required="true" type="OrderPosition"%>

<%-- any content can be specified here e.g.: --%>
<div class="card mb-4 shadow-1-strong">
    <div class="card-body">
        <div class="row">

            <div class="col-md-2 col-lg-1 agr-cart-position-image d-none d-md-block" 
                 style="background-image: url('/api/agrolavka/public/product-image/${position.product.id}" alt="${position.product.name}');">
            </div>

            <div class="col-sm-12 col-md-6 col-lg-8 ps-3 pe-3">
                <a href="<%= UrlProducer.buildProductUrl(position.getProduct())%>" class="agr-link"><h6>${position.product.name}</h6></a>
                <small class="text-muted agr-cart-position-description mb-2">
                    ${position.product.description}
                </small>
            </div>

            <div class="col-sm-12 col-md-4 col-lg-3 d-flex flex-column">
                <div class="d-flex justify-content-end">
                    <button class="btn btn-primary btn-floating me-2" type="button" data-product-quantity-plus>
                        <i class="fas fa-plus"></i>
                    </button>
                    <input type="number" class="form-control" aria-label="Количество" min="1" value="${position.quantity}"
                           data-product-id="${position.product.id}" data-product-quantity style="flex: 1">
                    <button class="btn btn-primary btn-floating me-2 ms-2" type="button" data-product-quantity-minus>
                        <i class="fas fa-minus"></i>
                    </button>
                    <button class="btn btn-danger btn-floating" data-remove-product-from-cart data-product-id="${position.product.id}"
                            type="button">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>

                <div style="flex: 1;" class="d-flex justify-content-end align-items-end">
                    <span class="text-dark fw-bold">
                        <%
                            String price = String.format("%.2f", position.getPrice());
                            String[] parts = price.split("\\.");
                            out.print(parts[0] + ".");
                            out.print("<small>" + parts[1] + "</small>");
                        %> <small class="text-muted">BYN</small></span>

                </div>

            </div>

        </div>
    </div>
</div>