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

    <div class="row">
        <%
            for (int i = 0; i < newProducts.size(); i++) {
                Product product = newProducts.get(i);
                if (i % 4 == 0) {
                    if (i > 0) {
                        out.print("</div>");
                    }
                    out.print("<div class=\"row\">");
                }
        %>
            <div class="col-sm-6 col-md-6 col-lg-3">
                <t:card-product product="<%= product %>" cart="${cart}"/>
            </div>
        <%
            }
            out.print("</div>");
        %>
    </div>
</section>