<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.agrolavka.util.UrlProducer,ss.entity.agrolavka.ProductsGroup"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="groups" required="false" type="java.util.List<ss.entity.agrolavka.ProductsGroup>"%>
<%@attribute name="label" required="false" type="java.lang.String"%>

<%-- any content can be specified here e.g.: --%>
<c:if test="${label != null}">
    <style>
        @media (max-width: 992px) {
            .agrolavka-breadcrumb > ol {
                margin-top: 1rem;
                display: flex;
                justify-content: center;
            }
        }
        @media (min-width: 992px) {
            .agrolavka-breadcrumb > ol {
                margin-bottom: 1.5rem;
            }
        }
    </style>
    <nav aria-label="breadcrumb" class="agrolavka-breadcrumb">
        <ol class="breadcrumb">
            <% 
                if (groups != null) {
                    for (ProductsGroup group : groups) { 
            %>
                        <li class="breadcrumb-item">
                            <a href="<%= UrlProducer.buildProductGroupUrl(group) %>">
                                <small><%= group.getName() %></small>
                            </a>
                        </li>
            <%
                    }
                }
            %>
            <li class="breadcrumb-item active" aria-current="page">
                <small>${label}</small>
            </li>
        </ol>
    </nav>
</c:if>