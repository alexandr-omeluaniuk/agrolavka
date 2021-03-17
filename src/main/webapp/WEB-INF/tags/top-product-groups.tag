<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup,java.util.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<style>
    .top-categories-dropdown {
        inset: unset !important;
        transform: none !important;
        width: 100%;
        top: 38px !important;
        min-height: 200px;
    }
</style>
<div id="subheader" class="collapse show" aria-expanded="true">
    <div class="d-flex align-items-center p-2 d-none d-lg-block">
        <div class="btn-group dropdown" role="group" aria-label="Избранные категории" style="width: 100%">
            <% 
                List<ProductsGroup> topCategories = UrlProducer.getTopCategories();
                for (ProductsGroup group : topCategories) {
            %>
            <button type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false"><%= group.getName() %></button>
            <%
                }
            %>
            <ul class="dropdown-menu top-categories-dropdown shadow-sm" aria-labelledby="dropdownMenuButton1">
                TODO: content
            </ul>
        </div>
        
    </div>
</div>