<%-- 
    Document   : product-groups-tree
    Created on : Mar 13, 2021, 8:23:59 PM
    Author     : alex
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="java.lang.*,java.util.*,ss.entity.agrolavka.ProductsGroup"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="groups" required="true" type="Set<ProductsGroup>"%>
<%@attribute name="groupId" required="false" type="Long"%>
<%
    Map<String, List<ProductsGroup>> groupsMap = new HashMap<>();
    List<ProductsGroup> roots = new ArrayList<>();
    for (ProductsGroup group : groups) {
        if (group.getParentId() != null) {
            if (!groupsMap.containsKey(group.getParentId())) {
                groupsMap.put(group.getParentId(), new ArrayList<>());
            }
            groupsMap.get(group.getParentId()).add(group);
        } else {
            roots.add(group);
        }
    }
    Collections.sort(roots);
    request.setAttribute("catalogRoots", roots);
    request.setAttribute("catalogMap", groupsMap);
%>
<%-- any content can be specified here e.g.: --%>
<style>
    .pgt-expand-icon {
        transition: transform 0.35s ease;
        transform-origin: 50% 50%;
        margin-right: 7px;
    }
    .pgt-root .btn[aria-expanded="true"] .pgt-expand-icon {
        transform: rotate(90deg);
    }
    .pgt-root .btn {
        width: 100%;
    }
    .pgt-root a {
        padding: .1875rem .5rem;
        margin-top: .125rem;
        color: rgba(0,0,0,0.65);
        text-decoration: none;
    }
    .pgt-root .btn:focus {
        box-shadow: 0 0 0 1px rgb(121 82 179 / 70%);
        background-color: rgba(121,82,179,0.1);
        font-weight: bold;
    }
    .pgt-root .active {
        font-weight: bold;
        color: rgba(0,0,0,0.85);
    }
</style>
<nav class="pgt-root">
    <h3><i class="fas fa-seedling nav-icon"></i> Каталог товаров</h3>
    <ul class="list-unstyled mb-0 py-3 pt-md-1">
        <c:forEach items="${catalogRoots}" var="root">
            <t:product-groups-tree-item group="${root}" catalogMap="${catalogMap}" groupId="${groupId}"/>
        </c:forEach>
    </ul>
</nav>
