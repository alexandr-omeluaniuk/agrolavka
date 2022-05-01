<%-- 
    Document   : product
    Created on : Feb 23, 2021, 10:23:56 AM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:app title="Наши магазины" metaDescription="Магазины Агролавка в Беларуси"
       canonical="/shops">
    
    <jsp:body>
        
        <main class="min-vh-100">
            <div class="container">
                <c:forEach items="${shops}" var="shop">
                    <t:shop-full-info shop="${shop}"/>
                </c:forEach>
            </div>
        </main>
    </jsp:body>
    
</t:app>
