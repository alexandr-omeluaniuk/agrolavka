<%-- 
    Document   : highlights
    Created on : Feb 16, 2021, 10:47:30 PM
    Author     : alex
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Purchase history" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="purchaseHistory" required="true" type="java.util.List<ss.entity.agrolavka.Order>"%>
<%-- any content can be specified here e.g.: --%>
<hr/>
<h5>Ранее заказывали:</h5>
<c:forEach items="${purchaseHistory}" var="order">
    <div class="row">
        <div class="col-12">
            <small class="text-muted">Заказ от ${order.formatCreated()}</small>
        </div>
    </div>

</c:forEach>
