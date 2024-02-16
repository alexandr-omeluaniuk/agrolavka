<%-- 
    Document   : product
    Created on : Feb 23, 2021, 10:23:56 AM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:app title="Наши магазины" metaDescription="Магазины Агролавка в Беларуси" canonical="/shops">
    <jsp:body>
        
        <main class="min-vh-100">
            <div class="container">
                <section class="pt-2 pb-5">
                    <h3 class="text-center">Магазины сети Агролавка в Беларуси</h3>
                </section>
                <div class="row ps-2 pe-2">
                <c:forEach items="${shops}" var="shop">
                    <div class="col-sm-12 col-md-6 col-lg-4">
                        <t:shop-card shop="${shop}"/>
                    </div>
                </c:forEach>
                </div>
            </div>
        </main>

    </jsp:body>
    
</t:app>
