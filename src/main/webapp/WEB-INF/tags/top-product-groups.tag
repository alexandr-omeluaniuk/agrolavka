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
    <h4 class="mb-4"><strong>Наша основная специализация это</strong></h4>

    <div class="row">
        <% 
            List<ProductsGroup> topCategories = UrlProducer.getTopCategories();
            for (ProductsGroup group : topCategories) {
        %>
        <div class="col-lg col-md col-sm-12">
            <t:card-category group="<%= group %>"/>
        </div>
        <%
            }
        %>
    </div>
    
    <h6 class="mb-2 mt-4"><strong>а также множество других товаров для сада и огорода</strong></h6>
    <a class="btn btn-outline-dark btn-lg m-2" href="/catalog" role="button" >Перейти в каталог товаров</a>
</section>