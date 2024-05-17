<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="isProduct" required="true" type="java.lang.Boolean"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<div class="card shadow-1-strong mb-4 agr-card-placeholder" aria-hidden="true">
    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
        <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/no-image.png')"></div>
        <div class="card-body agr-card-body-category">
            <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
            <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
            <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
            <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
            <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
            <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
            <a href="#" tabindex="-1" class="btn disabled placeholder col-12 bg-dark"></a>
        </div>
    </div>
</div>