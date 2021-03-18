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
        top: 46px !important;
        min-height: 200px;
        box-shadow: 0 5px 20px rgb(0 0 0 / 50%);
    }
    .top-categories-dropdown ul {
        list-style: none;
    }
    #subheader {
        font-family: 'Comfortaa', cursive;
    }
</style>
<div id="subheader" class="collapse show" aria-expanded="true">
    <div class="d-flex align-items-center p-2 d-none d-lg-block dropdown">
        <div class="btn-group" role="group" aria-label="Избранные категории" style="width: 100%">
            <% 
                List<ProductsGroup> topCategories = UrlProducer.getTopCategories();
                for (ProductsGroup group : topCategories) {
            %>
            <button type="button" class="btn btn-success" product-group="<%= group.getId() %>"><%= group.getName() %></button>
            <%
                }
            %>
        </div>
        <ul class="dropdown-menu top-categories-dropdown p-4">
            <%
                Map<String, List<ProductsGroup>> tree = UrlProducer.getCategoriesTree();
                for (ProductsGroup group : topCategories) {
            %>
            <div class="row" product-group="<%= group.getId() %>">
                <% 
                    if (tree.containsKey(group.getExternalId())) {
                        for (ProductsGroup subgroup : tree.get(group.getExternalId())) {
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
                            for (ProductsGroup secondLevelGroup : tree.get(subgroup.getExternalId())) {
                    %>
                        <a href="<%= UrlProducer.buildProductGroupUrl(secondLevelGroup) %>">
                            <li>
                                <small class="text-dark">- <%= secondLevelGroup.getName() %></small>
                            </li>
                        </a>
                    <%
                            }
                    %>
                        </ul>
                    <%
                        }
                    %>
                </div>
                <%
                        }
                    }
                %>
            </div>
            <%
                }
            %>
        </ul>
    </div>
</div>
<script>
    (function () {
        "use strict";
        
        document.querySelectorAll('#subheader .btn-group .btn').forEach(el => {
            el.addEventListener('click', function (e) {
                console.log(e.target.getAttribute("product-group"));
                const menu = document.querySelector(".top-categories-dropdown");
                menu.classList.add('show');
            });
        });
        
    })();
</script>