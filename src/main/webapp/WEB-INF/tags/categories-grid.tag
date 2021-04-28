<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="Categories grid" pageEncoding="UTF-8" 
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="categories" required="true" type="java.util.List<ss.entity.agrolavka.ProductsGroup>"%>

<%-- any content can be specified here e.g.: --%>
<div class="row">
    <c:forEach items="${categories}" var="group">
        <div class="col-lg-3 col-md-4 col-sm-12">
            <t:card-category group="${group}"/>
        </div>
    </c:forEach>
</div>