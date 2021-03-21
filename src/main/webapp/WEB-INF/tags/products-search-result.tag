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
<%@attribute name="group" required="false" type="ProductsGroup"%>

<%-- any content can be specified here e.g.: --%>
<style>
    .products-search-result a {
        font-family: "Open Sans", sans-serif;
    }
    @media (max-width: 992px) {
        .products-search-result {
            margin-top: 16px;
        }
        .product-description {
            max-width: 300px;
        }
    }
    .products-toolbar {
        margin-bottom: 10px;
    }
    .product-card {
        position: relative;
        box-shadow: rgb(0 0 0 / 20%) 0px 2px 1px -1px, rgb(0 0 0 / 14%) 0px 1px 1px 0px, rgb(0 0 0 / 12%) 0px 1px 3px 0px;
    }
    .product-card:hover {
        cursor: pointer;
        box-shadow: rgb(0 0 0 / 20%) 0px 3px 3px -2px, rgb(0 0 0 / 14%) 0px 3px 4px 0px, rgb(0 0 0 / 12%) 0px 1px 8px 0px !important;
    }
    .product-image {
        display: block;
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        height: 0;
        padding-top: 100%;
    }
    .product-description {
        max-width: 600px;
        text-overflow: ellipsis;
        overflow: hidden;
        display: inline-block;
    }
</style>
<div class="row products-search-result">
    <div class="col-sm-12">
        <% String url = ss.agrolavka.util.UrlProducer.buildProductGroupUrl(group); %>
        <t:products-search-result-pagination pages="${pages}" page="${page}" view="${view}" url="<%= url %>">
            
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
                                <div class="col-sm-12 col-md-6 col-lg-6 col-xl-4">
                                    <t:product-card product="${product}"></t:product-card>
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
        <t:products-search-result-pagination pages="${pages}" page="${page}" view="${view}" url="<%= url %>">
            
        </t:products-search-result-pagination>
    </div>
</div>