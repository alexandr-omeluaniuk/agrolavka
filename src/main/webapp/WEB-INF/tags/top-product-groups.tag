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
        min-height: 100px;
        box-shadow: 0 5px 20px rgb(0 0 0 / 50%);
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
    .category-level-2 {
        margin-left: 16px;
    }
    .category-level-1:hover, .category-level-2:hover {
        color: rgb(121,82,179) !important;
        border-bottom: 1px dotted rgb(121,82,179);
    }
    .top-categories-dropdown h6 {
        margin-top: .5rem;
    }
    .top-category-icon {
        margin-right: 7px;
    }
</style>
<div id="subheader" class="collapse show" aria-expanded="true">
    <div class="d-flex align-items-center d-none d-lg-block dropdown">
        <div class="btn-group shadow-sm" role="group" aria-label="Избранные категории" style="width: 100%">
            <% 
                List<ProductsGroup> topCategories = UrlProducer.getTopCategories();
                for (ProductsGroup group : topCategories) {
            %>
            <button type="button" class="btn btn-success" product-group="<%= group.getId() %>">
                <% if (group.getFaIcon() != null) {%> 
                    <i class="top-category-icon <%= group.getFaIcon() %>"></i>
                <% } %>
                <%= group.getName() %>
            </button>
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
                        final int columns = 3;
                        List<ProductsGroup> listLevel1 = tree.get(group.getExternalId());
                        // sort
                        Collections.sort(listLevel1);
                        List<ProductsGroup> listLevel1WithoutChilds = new ArrayList();
                        List<ProductsGroup> listLevel1WithChilds = new ArrayList();
                        Set<String> firstLevelCategoriesKeys = new HashSet();
                        for (ProductsGroup subgroup : listLevel1) {
                            firstLevelCategoriesKeys.add(subgroup.getExternalId());
                            if (tree.containsKey(subgroup.getExternalId())) {
                                listLevel1WithChilds.add(subgroup);
                            } else {
                                listLevel1WithoutChilds.add(subgroup);
                            }
                        }
                        listLevel1.clear();
                        for (ProductsGroup pg : listLevel1WithChilds) {
                            listLevel1.add(pg);
                            List<ProductsGroup> childs = tree.get(pg.getExternalId());
                            Collections.sort(childs);
                            for (ProductsGroup pgChild : childs) {
                                listLevel1.add(pgChild);
                            }
                        }
                        if (!listLevel1WithoutChilds.isEmpty()) {
                            ProductsGroup divider = new ProductsGroup();
                            divider.setName("разное");
                            divider.setId(-1L);
                            listLevel1.add(divider);
                        }
                        listLevel1.addAll(listLevel1WithoutChilds);
                        int linksInColumn = Double.valueOf(Math.ceil((double) listLevel1.size() / (double) columns)).intValue();
                        // group by columns
                        for (int counter = 0; counter < linksInColumn * columns; counter++) {
                            if (counter % linksInColumn == 0) {
                                if (counter > 0) {
                                    out.print("</div>");
                                }
                                out.print("<div class=\"col-sm-4\">");
                            }
                            ProductsGroup linkedGroup = listLevel1.size() > counter ? listLevel1.get(counter) : null;
                            if (linkedGroup != null) {
                                if (linkedGroup.getId() == -1) {
                                %>
                                    <h6 class="text-muted fw-bold">
                                        <span><%= linkedGroup.getName() %></span>
                                    </h6>
                                <%
                                } else if (firstLevelCategoriesKeys.contains(linkedGroup.getExternalId()) 
                                        && tree.containsKey(linkedGroup.getExternalId())) {
                                %> 
                                    <a href="<%= UrlProducer.buildProductGroupUrl(linkedGroup) %>">
                                        <h6 class="text-muted fw-bold">
                                            <span class="category-level-1"><%= linkedGroup.getName() %></span>
                                        </h6>
                                    </a>
                                <%
                                } else {
                                %>
                                    <a href="<%= UrlProducer.buildProductGroupUrl(linkedGroup) %>">
                                        <li>
                                            <small class="text-dark category-level-2">- <%= linkedGroup.getName() %></small>
                                        </li>
                                    </a>
                                <%
                                }
                            }
                        }
                        out.print("</div>");
                    }%>
            </div>
            <% } %>
        </ul>
    </div>
</div>
        
        
        <!--a href="">
                                            <li>
                                                <small class="text-dark">- </small>
                                            </li>
                                        </a-->
<script>
    (function () {
        "use strict";
        
        const dropdownTrigger = document.querySelector("#open-subcatalog-trigger");
        
        document.querySelectorAll('#subheader .btn-group .btn').forEach(el => {
            el.addEventListener('click', function (e) {
                e.stopPropagation();
                const selectedCategory = e.target.getAttribute("product-group");
                const slides = document.querySelectorAll(".top-categories-dropdown div[product-group]");
                slides.forEach(el => {
                    el.classList.add('d-none');
                });
                slides.forEach(el => {
                    if (el.getAttribute("product-group") === selectedCategory) {
                        el.classList.remove('d-none');
                    }
                });
                const dropdown = new bootstrap.Dropdown(dropdownTrigger);
                dropdown.show();
            });
        });
        
    })();
</script>