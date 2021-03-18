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
        top: 40px !important;
        min-height: 200px;
        box-shadow: 0 5px 20px rgb(0 0 0 / 50%);
    }
    .top-categories-dropdown ul {
        list-style: none;
    }
    #subheader {
        font-family: 'Comfortaa', cursive;
    }
    #subheader .btn-success {
        background-color: rgb(76, 175, 80);
        border-color: #289e06;
    }
    #subheader .btn-success:focus {
        font-weight: bold;
        box-shadow: rgb(0 0 0 / 20%) 0px 3px 1px -2px, rgb(0 0 0 / 14%) 0px 2px 2px 0px, rgb(0 0 0 / 12%) 0px 1px 5px 0px;
    }
</style>
<div id="subheader" class="collapse show" aria-expanded="true">
    <div class="d-flex align-items-center d-none d-lg-block dropdown">
        <div class="btn-group shadow-sm" role="group" aria-label="Избранные категории" style="width: 100%">
            <% 
                List<ProductsGroup> topCategories = UrlProducer.getTopCategories();
                for (ProductsGroup group : topCategories) {
            %>
            <button type="button" class="btn btn-success" product-group="<%= group.getId() %>"><%= group.getName() %></button>
            <%
                }
            %>
        </div>
        <button class="btn btn-secondary dropdown-toggle" type="button" id="open-subcatalog-trigger" style="display: none;"
                data-bs-toggle="dropdown" aria-expanded="false">
            Открыть подкаталог
        </button>
        <ul class="dropdown-menu top-categories-dropdown p-4" aria-labelledby="open-subcatalog-trigger">
            <%
                Map<String, List<ProductsGroup>> tree = UrlProducer.getCategoriesTree();
                for (ProductsGroup group : topCategories) {
            %>
            <div class="row" product-group="<%= group.getId() %>">
                <% 
                    if (tree.containsKey(group.getExternalId())) {
                        List<ProductsGroup> listLevel1 = tree.get(group.getExternalId());
                        Collections.sort(listLevel1);
                        for (ProductsGroup subgroup : listLevel1) {
                %>
                <div class="col-sm-4">
                    <a href="<%= UrlProducer.buildProductGroupUrl(subgroup) %>">
                        <h6 class="text-muted fw-bold"><%= subgroup.getName() %></h6>
                    </a>
                    <%
                        if (tree.containsKey(subgroup.getExternalId())) {
                    %>
                        <ul class="mb-4">
                    <%
                        List<ProductsGroup> listLevel2 = tree.get(subgroup.getExternalId());
                        Collections.sort(listLevel2);
                        for (ProductsGroup secondLevelGroup : listLevel2) {
                    %>
                        <a href="<%= UrlProducer.buildProductGroupUrl(secondLevelGroup) %>">
                            <li>
                                <small class="text-dark">- <%= secondLevelGroup.getName() %></small>
                            </li>
                        </a>
                    <% } %>
                        </ul>
                    <% } %>
                </div>
                <% } } %>
            </div>
            <% } %>
        </ul>
    </div>
</div>
<script>
    (function () {
        "use strict";
        
        //const dropdown = document.querySelector('#subheader .top-categories-dropdown');
        const dropdownTrigger = document.querySelector("#open-subcatalog-trigger");
        
        
        document.querySelectorAll('#subheader .btn-group .btn').forEach(el => {
            el.addEventListener('click', function (e) {
                const selectedCategory = e.target.getAttribute("product-group");
                document.querySelectorAll(".top-categories-dropdown div[product-group]").forEach(el => {
                    if (el.getAttribute("product-group") === selectedCategory) {
                        el.classList.remove('d-none');
                    } else {
                        el.classList.add('d-none');
                    }
                });
                const dropdown = new bootstrap.Dropdown(dropdownTrigger);
                dropdown.show();
                e.stopPropagation();
            });
        });
        
    })();
</script>