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
<%@tag description="Product card" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.*,ss.agrolavka.util.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="attribute" required="true" type="ProductAttribute"%>

<div class="card shadow-1-strong mb-4 hover-shadow">
    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
        <div class="card-body agr-card-body-category">
            <h6 class="card-title mb-0 text-dark">${attribute.name}</h6>
        </div>
    </div>
</div>
