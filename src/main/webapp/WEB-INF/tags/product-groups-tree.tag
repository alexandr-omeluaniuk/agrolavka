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
<%@attribute name="groups" required="true" type="List<ProductsGroup>"%>
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
<nav class="pgt-root">
    <h4><i class="fas fa-seedling nav-icon"></i> Каталог товаров</h4>
    <ul class="list-unstyled mb-0 py-3 pt-md-1">
        <c:forEach items="${catalogRoots}" var="root">
            <t:product-groups-tree-item group="${root}" catalogMap="${catalogMap}" groupId="${groupId}"/>
        </c:forEach>
    </ul>
</nav>
