<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="Product highlights" pageEncoding="UTF-8" import="ss.entity.agrolavka.*"%>
<%@attribute name="newProducts" required="true" type="java.util.List<Product>"%>
<%@attribute name="cart" required="true" type="Order"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<section class="text-center">
    <h4 class="mb-4"><strong>Новинки</strong></h4>
    <h6 class="mb-3">Последнее поступление товаров</h6>
    <!-- Carousel wrapper -->
    <div id="agr-new-products-carousel" class="carousel slide carousel-fade" data-mdb-ride="carousel">
        <!-- Indicators -->
        <!--div class="carousel-indicators">
            <button type="button" data-mdb-target="#agr-new-products-carousel" data-mdb-slide-to="0" class="active"
                    aria-current="true" aria-label="Slide 1"></button>
            <button type="button" data-mdb-target="#agr-new-products-carousel" data-mdb-slide-to="1" 
                    aria-label="Slide 2"></button>
            <button type="button" data-mdb-target="#agr-new-products-carousel" data-mdb-slide-to="2" 
                    aria-label="Slide 3"></button>
        </div-->
        <!-- Inner -->
        <div class="carousel-inner p-1">
            <%
                for (int i = 0; i < newProducts.size(); i++) {
                    Product product = newProducts.get(i);
                    if (i % 4 == 0) {
                        if (i > 0) {
                            out.print("</div></div>");
                        }
                        out.print("<div class=\"carousel-item " + (i == 0 ? "active" : "") + "\"><div class=\"row\">");
                    }
            %>
            <div class="col-lg col-md-6 col-sm-6 col-6">
                <t:card-product product="<%= product%>" cart="${cart}" showCreatedDate="true"/>
            </div>
            <%
                }
                out.print("</div>");
            %>
        </div>
        <!-- Inner -->

        <!-- Controls -->
        <button class="carousel-control-prev d-none" type="button" data-mdb-target="#agr-new-products-carousel" data-mdb-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next d-none" type="button" data-mdb-target="#agr-new-products-carousel" data-mdb-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
    
</section>