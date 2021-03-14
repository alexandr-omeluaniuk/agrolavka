<%-- 
    Document   : products-search-result
    Created on : Mar 14, 2021, 2:49:35 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="java.lang.*,java.util.*,ss.entity.agrolavka.*,ss.agrolavka.constants.SiteConstants"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="searchResult" required="true" type="List<Product>"%>
<%@attribute name="pages" required="true" type="Integer"%>
<%@attribute name="page" required="true" type="Integer"%>
<%@attribute name="view" required="true" type="String"%>
<%@attribute name="groupId" required="false" type="Long"%>

<%-- any content can be specified here e.g.: --%>
<style>
    @media (max-width: 992px) {
        .products-search-result {
            margin-top: 16px;
        }
    }
    .products-toolbar {
        margin-bottom: 10px;
    }
    .product-card:hover {
        cursor: pointer;
        box-shadow: 0 .5rem 1rem rgba(0,0,0,.15) !important;
    }
    .product-image {
        display: block;
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        height: 0;
        padding-top: 100%;
    }
</style>
<div class="row products-search-result">
    <div class="col-sm-12">
        <% String url = groupId != null ? ("/catalog/" + groupId) : "/catalog"; %>
        <t:products-search-result-pagination pages="${pages}" page="${page}" view="${view}" url="<%= url %>">
            
        </t:products-search-result-pagination>
        <c:choose>
            <c:when test="${searchResult.isEmpty()}">
                <p class="text-center">По вашему запросу ничего не найдено</p>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${'TILES' == view}">
                        <% int counter = 0; %>
                        <c:forEach step="1" begin="1" end="${SiteConstants.SEARCH_RESULT_TILES_ROWS}">
                            <div class="row">
                                <c:forEach step="1" begin="1" end="${SiteConstants.SEARCH_RESULT_TILES_COLUMNS}">
                                    <div class="col-sm">
                                        <% if (searchResult.size() > counter) { %>
                                            <t:product-card product="<%= searchResult.get(counter) %>"></t:product-card>
                                        <% } %>
                                    </div>
                                    <% 
                                        counter++;
                                    %>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <t:products-search-result-pagination pages="${pages}" page="${page}" view="${view}" url="<%= url %>">
            
        </t:products-search-result-pagination>
    </div>
</div>