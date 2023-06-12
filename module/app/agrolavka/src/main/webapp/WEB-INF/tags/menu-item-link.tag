<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="link" required="true" type="java.lang.String"%>
<%@attribute name="label" required="true" type="java.lang.String"%>
<%@attribute name="icon" required="false" type="java.lang.String"%>
<%@attribute name="image" required="false" type="java.lang.String"%>
<%@attribute name="imageAlt" required="false" type="java.lang.String"%>
<%@attribute name="iconColor" required="true" type="java.lang.String"%>
<%@attribute name="labelColor" required="false" type="java.lang.String"%>
<%@attribute name="isCatalogLink" required="false" type="java.lang.Boolean"%>

<%-- any content can be specified here e.g.: --%>
<a href="${link}" class="p-1 agr-mobile-menu-link" ${isCatalogLink == null ? "" : "data-catalog=\"-1\""}>
    <div class="list-group-item list-group-item-action d-flex rounded-pill p-2 align-items-center">
        <button class="btn btn-sm btn-${iconColor} btn-floating me-2" type="button">
            <i class="fas fa-${icon} fa-fw me-3"></i>
        </button>
        <c:if test="${empty isCatalogLink}">
            <span class="text-${labelColor}" style="flex: 1;">${label}</span>
        </c:if>
        
        <c:if test="${isCatalogLink == true}">
            <b class="agr-menu-item-higlight" style="flex: 1;">${label}</b>
            <i class="fas fa-chevron-right text-dark me-2"></i>
        </c:if>
    </div>
</a>