<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup,java.util.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<section class="text-center">
    <h4 class="mb-5"><strong>Наша специализация</strong></h4>

    <div class="row">
        <% 
            List<ProductsGroup> topCategories = UrlProducer.getTopCategories();
            for (ProductsGroup group : topCategories) {
        %>
        <div class="col-lg col-md col-sm-12">
            <div class="card">
                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                    <img
                        src="https://mdbootstrap.com/img/new/standard/nature/184.jpg"
                        class="img-fluid"
                        />
                    <a href="#!">
                        <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>
                    </a>
                </div>
                <div class="card-body">
                    <h5 class="card-title">Card title</h5>
                    <p class="card-text">
                        Some quick example text to build on the card title and make up the bulk of the
                        card's content.
                    </p>
                    <a href="#!" class="btn btn-primary">Button</a>
                </div>
            </div>
        </div>
        <%
            }
        %>
        

    </div>
</section>