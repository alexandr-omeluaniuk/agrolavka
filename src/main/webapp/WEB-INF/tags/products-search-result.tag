<%-- 
    Document   : products-search-result
    Created on : Mar 14, 2021, 2:49:35 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="java.lang.*,java.util.*,ss.entity.agrolavka.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="searchResult" required="true" type="List<Product>"%>
<%@attribute name="pages" required="true" type="Integer"%>
<%@attribute name="page" required="true" type="Integer"%>
<%@attribute name="view" required="true" type="String"%>
<%@attribute name="sort" required="true" type="String"%>
<%@attribute name="group" required="false" type="ProductsGroup"%>
<%@attribute name="cart" required="true" type="Order"%>

<%-- any content can be specified here e.g.: --%>
<div class="row products-search-result">
    <div class="col-sm-12">
        <% String url = ss.agrolavka.util.UrlProducer.buildProductGroupUrl(group); %>
        <t:products-search-result-pagination pages="${pages}" page="${page}" view="${view}" sort="${sort}" url="<%= url %>"
                                             available="${available}">
            
        </t:products-search-result-pagination>
        <c:choose>
            <c:when test="${searchResult.isEmpty()}">
                <p class="text-center">По вашему запросу ничего не найдено</p>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${'TILES' == view}">
                        <div class="row">
                            <c:forEach items="${searchResult}" var="product">
                                <div class="col-sm-6 col-md-6 col-lg-4 col-xl-3 col-6">
                                    <t:card-product product="${product}" cart="${cart}" />
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group mb-3">
                            <c:forEach items="${searchResult}" var="product">
                                <t:product-list-item product="${product}"></t:product-list-item>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <t:products-search-result-pagination pages="${pages}" page="${page}" view="${view}" sort="${sort}" url="<%= url %>"
                                             available="${available}">
            
        </t:products-search-result-pagination>
    </div>
</div>