<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup,java.util.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<div id="subheader" class="collapse show">
    <div class="d-flex align-items-center d-none d-lg-block dropdown">
        <div class="btn-group shadow-sm" role="group" aria-label="Избранные категории" style="width: 100%">
            <% 
                List<ProductsGroup> topCategories = UrlProducer.getTopCategories();
                for (ProductsGroup group : topCategories) {
            %>
            <button type="button" class="btn btn-success" data-product-group="<%= group.getId() %>">
                <% if (group.getFaIcon() != null) {%> 
                    <i class="top-category-icon <%= group.getFaIcon() %>" data-product-group="<%= group.getId() %>"></i>
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
        <div class="dropdown-menu top-categories-dropdown p-4" aria-labelledby="open-subcatalog-trigger">
            <%
                Map<String, List<ProductsGroup>> tree = UrlProducer.getCategoriesTree();
                for (ProductsGroup group : topCategories) {
            %>
            <div class="row" data-product-group="<%= group.getId() %>">
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
                                        <p class="mb-0">
                                            <small class="text-dark category-level-2">- <%= linkedGroup.getName() %></small>
                                        </p>
                                    </a>
                                <%
                                }
                            }
                        }
                        out.print("</div>");
                    }%>
            </div>
            <% } %>
        </div>
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
                const selectedCategory = e.target.getAttribute("data-product-group");
                const slides = document.querySelectorAll(".top-categories-dropdown div[data-product-group]");
                slides.forEach(el => {
                    el.classList.add('d-none');
                });
                slides.forEach(el => {
                    if (el.getAttribute("data-product-group") === selectedCategory) {
                        el.classList.remove('d-none');
                    }
                });
                const dropdown = new bootstrap.Dropdown(dropdownTrigger);
                dropdown.show();
            });
        });
        
    })();
</script>