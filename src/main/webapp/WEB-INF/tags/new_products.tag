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
<div class="container h-100">
    <div class="row d-flex justify-content-center">
        <div class="col-lg-12 intro-info order-lg-first order-last">
            <h1 class="text-center">Новинки</h1>
        </div>
    </div>
    <div class="row d-flex justify-content-center">
        <div class="col-sm-12 intro-info order-lg-first order-last">
            <h3 class="text-center">Последнее поступление товаров</h3>
            <hr/>
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
                <div class="col-sm-3">
                    <t:product-card product="<%= product %>" mini="true" cart="${cart}"/>
                </div>
            <%
                }
                out.print("</div>");
            %>
        </div>
    </div>
</div>