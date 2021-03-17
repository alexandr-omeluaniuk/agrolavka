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
</style>
<div id="subheader" class="collapse show" aria-expanded="true">
    <div class="d-flex align-items-center p-2 d-none d-lg-block">
        <div class="btn-group dropdown" role="group" aria-label="Избранные категории" style="width: 100%">
            <% 
                List<ProductsGroup> topCategories = UrlProducer.getTopCategories();
                for (ProductsGroup group : topCategories) {
            %>
            <button type="button" class="btn btn-success" data-bs-toggle="dropdown" aria-expanded="false"><%= group.getName() %></button>
            <%
                }
            %>
            <ul class="dropdown-menu top-categories-dropdown p-4">
                <div class="row">
                    <% 
                        Map<String, List<ProductsGroup>> tree = UrlProducer.getCategoriesTree();
                        for (ProductsGroup group : topCategories) {
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
                        }
                    %>
                    <!--div class="col-sm">
                        <a href="#"><h6 class="text-muted fw-bold">Категория 1</h6></a>
                        <ul class="mb-4">
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                        </ul>
                        <a href="#"><h6 class="text-muted fw-bold">Категория X</h6></a>
                        <ul class="mb-4">
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                        </ul>
                    </div>
                    <div class="col-sm">
                        <a href="#"><h6 class="text-muted fw-bold">Категория 2</h6></a>
                        <ul class="mb-4">
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                        </ul>
                    </div>
                    <div class="col-sm">
                        <a href="#"><h6 class="text-muted fw-bold">Категория 3</h6></a>
                        <ul class="mb-4">
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                            <a href="#"><li><small class="text-dark">- Подкатегория</small></li></a>
                        </ul>
                    </div-->
                </div>
            </ul>
        </div>
        
    </div>
</div>