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
<%@attribute name="type" required="true" type="java.lang.String"%>

<%-- any content can be specified here e.g.: --%>
<a href="${link}" class="p-1 d-flex align-items-center agr-external-link" target="_blank" rel="noreferrer">
    <div class="list-group-item list-group-item-action d-flex rounded-pill p-2 align-items-center">
        <c:if test="${type == 'Viber'}">
            <div class="agr-viber">
                <i class="fab fa-viber"></i>
            </div>
        </c:if>
        <c:if test="${type == 'Instagram'}">
            <img src="/assets/img/instagram.ico" alt="Instagram">
        </c:if>
        <span class="ms-2 text-dark" style="flex: 1;">${label}</span>
        <i class="fas fa-external-link-alt me-2"></i>
    </div>
</a>