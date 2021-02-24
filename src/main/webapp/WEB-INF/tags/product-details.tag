<%-- 
    Document   : product-details
    Created on : Feb 23, 2021, 8:45:36 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="ss.agrolavka.model.Product"%>

<%-- any content can be specified here e.g.: --%>
<div class="row">
    <div class="col-lg-6 col-sm-12">
        <div class="card shadow-sm mb-5 bg-body rounded">
            <div class="card-img-top product-image" style="background-image: url('/api/product-image/${product.getId()}')"></div>
        </div>
    </div>
    <div class="col-lg-6 col-sm-12">
        <table class="table table-bordered rounded">
            <tbody>
                <tr>
                    <td>Категория</td>
                    <td style="text-align: right;">${product.getGroup().getName()}</td>
                </tr>
                <tr>
                    <td>Цена</td>
                    <td style="text-align: right;"><% out.print(String.format("%.2f", product.getPrice())); %> BYN</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>