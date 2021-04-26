<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup,java.util.*"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

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
            <t:products-group-card group="<%= group %>"/>
        </div>
        <%
            }
        %>
        

    </div>
</section>