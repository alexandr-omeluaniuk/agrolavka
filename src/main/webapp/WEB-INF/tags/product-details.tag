<%-- 
    Document   : product-details
    Created on : Feb 23, 2021, 8:45:36 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="ss.entity.agrolavka.Product"%>

<%-- any content can be specified here e.g.: --%>
<style>
    .product-image {
        display: block;
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        height: 0;
        padding-top: 100%;
        width: 300px;
    }
    .product-description {
        white-space: pre-line;
        text-align: justify;
    }
</style>
<article>
    <div class="row">
        <div class="col-sm-12">
            <div class="card shadow-sm mb-2 me-4 bg-body rounded float-start">
                <div class="ribbon ribbon-top-left">
                    <span class="${product.quantity > 0 ? 'bg-success' : 'bg-danger'}">
                        <small>${product.quantity > 0 ? 'в наличии' : 'под заказ'}</small>
                    </span>
                </div>
                <div class="card-img-top product-image" 
                     style="background-image: url('/api/agrolavka/public/product-image/${product.getId()}')"></div>
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <small>Цена</small>
                        <b><% out.print(String.format("%.2f", product.getPrice()));%> BYN</b>
                    </div>
                </div>
            </div>
            <h4>${product.name}</h4>
            <p class="product-description">${product.description}</p>
        </div>
    </div>
</article>