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
    
    <jsp:attribute name="headSection">
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.8.0/dist/leaflet.css" integrity="sha512-hoalWLoI8r4UszCkZ5kL8vayOGVae1oxXe/2A4AO6J9+580uKHDO3JdHb7NzwwzK5xr/Fs0W40kiNHxM9vyTtQ==" crossorigin="" />
        <script src="https://unpkg.com/leaflet@1.8.0/dist/leaflet.js" integrity="sha512-BB3hKbKWOc9Ez/TAwyWxNXeoV9c1v6FIeYiBieIWkpLjauysF18NzgR1MBNBXf8/KABdlkX68nAhlwcDFLGPCQ==" crossorigin=""></script>
    </jsp:attribute>
    
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
