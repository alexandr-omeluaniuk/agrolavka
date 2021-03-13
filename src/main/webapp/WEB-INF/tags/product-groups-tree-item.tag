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
<%@attribute name="level" required="true" type="Integer"%>
<%@attribute name="catalogMap" required="true" type="Map<String, List<ProductsGroup>>"%>

<%-- any content can be specified here e.g.: --%>
    <c:choose>
        <c:when test="${catalogMap.containsKey(group.getExternalId())}">
            <button class="btn d-inline-flex align-items-center rounded collapsed" data-bs-toggle="collapse" 
                    data-bs-target="#product-group-item-${group.id}" aria-expanded="false">
                <i class="fas fa-chevron-right pgt-expand-icon"></i> ${group.name}
            </button>
            <div class="collapse" id="product-group-item-${group.id}" style="margin-left: ${10 * level}px">
                <ul class="list-unstyled fw-normal pb-1 small">
                    <c:forEach items="${catalogMap.get(group.getExternalId())}" var="child">
                        <t:product-groups-tree-item group="${child}" catalogMap="${catalogMap}" level="${level + 1}"/>
                    </c:forEach>
                </ul>
            </div>
        </c:when>    
        <c:otherwise>
            <li>
                <a href="/catalog/${group.id}?name=${group.name}" class="d-inline-flex align-items-center rounded">
                    ${group.name}
                </a>
            </li>
        </c:otherwise>
    </c:choose>