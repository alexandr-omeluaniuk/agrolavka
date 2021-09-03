<%-- 
    Document   : catalog
    Created on : Feb 18, 2021, 9:04:42 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="Товары на акции" metaDescription="Товары для сада и огорода на акции, купить дешево" canonical="/promotions">
    
    <jsp:body>
        <main class="min-vh-100">
            <div class="container mb-5">
                
                <section>
                    <h3 class="text-center mb-4">Товары на акции</h3>
                    <p class="text-uppercase text-muted text-center">Покупайте товары для сада и огорода дешевле чем раньше!</p>
                </section>
                
                <section class="m-2">
                    <c:choose>
                        <c:when test="${products.isEmpty()}">
                            <p class="text-center">В данный момент товаров на акции нет</p>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <c:forEach items="${products}" var="product">
                                    <div class="col-sm-6 col-md-6 col-lg-4 col-xl-3 col-6">
                                        <t:card-product product="${product}" cart="${cart}"/>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>
            </div>
        </main>
    </jsp:body>

</t:app>
