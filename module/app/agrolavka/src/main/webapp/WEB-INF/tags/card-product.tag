<%-- 
    Document   : product-card
    Created on : Mar 14, 2021, 4:25:15 PM
    Author     : alex
--%>

<%@tag import="org.json.JSONObject"%>
<%@tag import="org.json.JSONArray"%>
<%@tag import="java.text.SimpleDateFormat"%>
<%@tag import="java.util.Objects"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.*,ss.agrolavka.util.UrlProducer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%@attribute name="cart" required="true" type="Order"%>
<%@attribute name="noHover" required="false" type="Boolean"%>
<%@attribute name="showCreatedDate" required="false" type="Boolean"%>

<%-- any content can be specified here e.g.: --%>
<a href="<%= UrlProducer.buildProductUrl(product) %>">
    <div class="card shadow-1-strong mb-4 ${noHover ? '' : 'hover-shadow'} ${product.quantity > 0 ? '' : 'agr-product-not-available'}">
        <t:product-ribbon product="${product}"></t:product-ribbon>
        <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
            <c:choose>
                <c:when test="${product.images.size() > 0}">
                    <div class="card-img-top agr-card-image" style="background-image: url('/media/${product.images.get(0).fileNameOnDisk}?timestamp=${product.images.get(0).createdDate}')"></div>
                </c:when>
                <c:otherwise>
                    <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/no-image.png')"></div>
                </c:otherwise>
            </c:choose>
                    
                <!--div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div-->

            <div class="card-body" style="min-height: 100px;">
                <h6 class="card-title text-dark text-left" style="min-height: 60px;">${product.name}</h6>
                <t:product-price product="${product}" rowClass="agr-card-line"></t:product-price>
                
                <c:if test="${showCreatedDate}">
                    <div class="d-flex align-items-center justify-content-between mb-1 agr-card-line">
                        <small class="text-muted">Добавлено</small>
                        <small class="text-muted">
                            <%
                                out.print(new SimpleDateFormat("dd.MM.yyyy").format(product.getCreatedDate()));
                            %>
                        </small>
                    </div>
                </c:if>
                
                <t:product-actions cart="${cart}" product="${product}" buttonClass="agr-card-button"></t:product-actions>
            </div>
        </div>
    </div>
</a>