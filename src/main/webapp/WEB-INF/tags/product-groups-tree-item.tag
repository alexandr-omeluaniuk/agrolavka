<%-- 
    Document   : product-groups-tree-item
    Created on : Mar 13, 2021, 8:59:19 PM
    Author     : alex
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.entity.agrolavka.ProductsGroup,java.util.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="group" required="true" type="ProductsGroup"%>
<%@attribute name="catalogMap" required="true" type="Map<String, List<ProductsGroup>>"%>
<%@attribute name="groupId" required="false" type="java.lang.Long"%>

<%-- any content can be specified here e.g.: --%>
<%!
    public boolean isExpanded(ProductsGroup group, Map<String, List<ProductsGroup>> groupsMap) {
        if (groupId == null || !groupsMap.containsKey(group.getExternalId())) {
            return false;
        } else {
            List<ProductsGroup> childs = groupsMap.get(group.getExternalId());
            boolean isExpanded = false;
            for (ProductsGroup child : childs) {
                if (groupId.equals(child.getId()) || isExpanded(child, groupsMap)) {
                    isExpanded = true;
                    break;
                }
            }
            return isExpanded;
        }
    }
%>
<%
    request.setAttribute("expanded", isExpanded(group, catalogMap));
%>
<c:choose>
    <c:when test="${catalogMap.containsKey(group.getExternalId())}">
        <button class="btn d-inline-flex align-items-center rounded ${expanded ? "" : "collapsed"}" data-bs-toggle="collapse" 
                data-bs-target="#product-group-item-${group.id}" aria-expanded="${expanded}">
            <i class="fas fa-chevron-right pgt-expand-icon"></i> ${group.name}
        </button>
        <div class="collapse ${expanded ? "show" : ""}" id="product-group-item-${group.id}" style="margin-left: 1.4rem">
            <ul class="list-unstyled fw-normal pb-1 small">
                <c:forEach items="${catalogMap.get(group.getExternalId())}" var="child">
                    <t:product-groups-tree-item group="${child}" catalogMap="${catalogMap}" groupId="${groupId}"/>
                </c:forEach>
            </ul>
        </div>
    </c:when>    
    <c:otherwise>
        <li>
            <a href="/catalog/${group.id}?name=${group.name}" 
                    class="d-inline-flex align-items-center rounded ${group.getId().equals(groupId) ? "active" : ""}">
                ${group.name}
            </a>
        </li>
    </c:otherwise>
</c:choose>