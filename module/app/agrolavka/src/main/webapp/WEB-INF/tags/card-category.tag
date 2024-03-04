<%-- 
    Document   : product-card
    Created on : Mar 14, 2021, 4:25:15 PM
    Author     : alex
--%>

<%@tag import="java.util.Objects"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.*,ss.agrolavka.util.*,java.util.List,java.lang.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="group" required="true" type="ProductsGroup"%>

<%
    final var sb = new StringBuilder("[");
    final var nestedGroups = AppCache.getCategoriesTree().get(group.getExternalId());
    Boolean hasMore = false;
    if (nestedGroups != null && !nestedGroups.isEmpty()) {
        java.util.Collections.sort(nestedGroups);
        for (int i = 0; i < nestedGroups.size(); i++) {
            if (i == 4) {
                hasMore = true;
                break;
            }
            final var g = nestedGroups.get(i);
            sb.append("{'key':'")
                .append(g.getName())
                .append("','value':'")
                .append(UrlProducer.buildProductGroupUrl(g))
                .append("'},");
        }
        sb.setLength(sb.length() - 1);
    }
    sb.append("]");
%>

<%-- any content can be specified here e.g.: --%>
<x-agr-category-card 
    data-name="${group.name}"
    data-nested-groups="<%=sb.toString()%>"
    data-has-more-nested-groups="<%=hasMore%>"
    data-image="${group.images.size() > 0 ? group.images.get(0).fileNameOnDisk : ""}"
    data-image-created="${group.images.size() > 0 ? group.images.get(0).createdDate : ""}"
    data-link="<%= UrlProducer.buildProductGroupUrl(group)%>">
    <t:card-placeholder isProduct="false"></t:card-placeholder>
</x-agr-category-card>
