<%-- 
    Document   : discounts-highlight
    Created on : Sep 2, 2021, 7:57:26 PM
    Author     : alex
--%>

<%@tag description="discount highlight" pageEncoding="UTF-8" import="ss.entity.agrolavka.*, java.util.*"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="products" required="true" type="List<Product>"%>

<%-- any content can be specified here e.g.: --%>
<section class="text-center">
    <h4 class="mb-4"><strong>Акции</strong></h4>
    <h6 class="mb-3">Покупайте товары дешевле чем обычно!</h6>
    <!-- Carousel wrapper -->
    <div id="agr-product-discounts-carousel" class="carousel slide carousel-fade" data-mdb-ride="carousel">
        <!-- Inner -->
        <div class="carousel-inner p-1">
            <%
                final int SIZE = 4;
                List<List<Product>> rows = new ArrayList<>();
                List<Product> row = new ArrayList<>();
                for (int i = 0; i < products.size(); i++) {
                    if (row.size() == SIZE) {
                        rows.add(row);
                        row = new ArrayList<>();
                    }
                    row.add(products.get(i));
                }
                if (!rows.contains(row)) {
                    rows.add(row);
                }
                for (List<Product> r : rows) {
                    out.print("<div class=\"carousel-item " + (rows.indexOf(r) == 0 ? "active" : "") + "\"><div class=\"row\">");
                    for (Product product : r) {
                        %>
                            <div class="col-lg col-md-6 col-sm-6 col-6">
                                <t:card-product product="<%= product%>" cart="${cart}"/>
                            </div>
                        <%
                    }
                    if (r.size() != SIZE) {
                        int empty = SIZE - r.size();
                        while (empty > 0) {
                            out.print("<div class=\"col-lg col-md-6 col-sm-6 col-6\"></div>");
                            empty--;
                        }
                    }
                    out.print("</div></div>");
                }
            %>
        </div>
        <!-- Inner -->

        <!-- Controls -->
        <button class="carousel-control-prev d-none" type="button" data-mdb-target="#agr-product-discounts-carousel" data-mdb-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next d-none" type="button" data-mdb-target="#agr-product-discounts-carousel" data-mdb-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
    
    <h6 class="mb-2 mt-4"><strong>нажмите, чтобы просмотреть полный список акционных товаров</strong></h6>
    <a class="btn btn-outline-danger btn-lg m-2" href="/promotions" role="button" ><i class="fas fa-fire me-1"></i> Все товары на акции</a>
</section>