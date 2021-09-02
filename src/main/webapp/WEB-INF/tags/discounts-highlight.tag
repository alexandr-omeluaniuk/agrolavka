<%-- 
    Document   : discounts-highlight
    Created on : Sep 2, 2021, 7:57:26 PM
    Author     : alex
--%>

<%@tag description="discount highlight" pageEncoding="UTF-8" import="ss.entity.agrolavka.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="products" required="true" type="java.util.List<Product>"%>

<%-- any content can be specified here e.g.: --%>
<section class="text-center">
    <h4 class="mb-4"><strong>Акции</strong></h4>
    <h6 class="mb-3">Покупайте товары дешевле чем обычно!</h6>
    <!-- Carousel wrapper -->
    <div id="agr-product-discounts-carousel" class="carousel slide carousel-fade" data-mdb-ride="carousel">
        <!-- Inner -->
        <div class="carousel-inner p-1">
            <%
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);
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
        <button class="carousel-control-prev" type="button" data-mdb-target="#agr-product-discounts-carousel" data-mdb-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-mdb-target="#agr-product-discounts-carousel" data-mdb-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
    
</section>