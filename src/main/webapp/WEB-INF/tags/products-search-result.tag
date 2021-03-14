<%-- 
    Document   : products-search-result
    Created on : Mar 14, 2021, 2:49:35 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" import="java.lang.*,java.util.*,ss.entity.agrolavka.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="searchResult" required="true" type="List<Product>"%>
<%@attribute name="pages" required="true" type="Integer"%>
<%@attribute name="page" required="true" type="Integer"%>
<%@attribute name="view" required="true" type="String"%>

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
</style>
<div class="row products-search-result">
    <div class="col-sm-12">
        <t:products-search-result-pagination pages="${pages}" page="${page}" view="${view}"></t:products-search-result-pagination>
    </div>
</div>