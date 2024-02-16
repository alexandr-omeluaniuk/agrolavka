<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.Shop,ss.entity.images.storage.EntityImage,ss.agrolavka.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="shop" required="true" type="ss.entity.agrolavka.Shop"%>

<%-- any content can be specified here e.g.: --%>
<div class="card shadow-1-strong mb-4 hover-shadow">
    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
        <c:if test="${shop.images.size() > 0}">
                <div class="card-img-top agr-card-image"
                    style="background-image: url('/media/${shop.images.get(0).fileNameOnDisk}?timestamp=${shop.images.get(0).createdDate}')"></div>
            </c:if>
            <c:if test="${shop.images.size() == 0}">
                <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/no-image.png')"></div>
            </c:if>
        <a href="/shops/${UrlProducer.transliterate(shop.title.toLowerCase())}">
            <div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div>
        </a>
        <div class="card-body p-3">
            <h4 class="card-title mb-0">${shop.title}</h4>
            <p class="mt-1 text-muted" style="white-space: pre-line;">${shop.address}</p>
            <h6>Время работы</h6>
            <%
                final String workingHours = shop.getWorkingHours();
                final String[] lines = workingHours.split("\n");
                for (final String line : lines) {
                    if (line.contains("|")) {
                        final String subline1 = line.substring(0, line.lastIndexOf("|"));
                        final String subline2 = line.substring(line.lastIndexOf("|") + 1);
                        out.print("<div class=\"d-flex justify-content-between align-items-center\">");
                        out.print("<small>" + subline1 + "</small> <small>" + subline2 + "</small>");
                        out.print("</div>");
                    } else {
                        out.print("<p style=\"white-space: pre;\">${shop.workingHours}</p>");
                    }
                }
            %>
            <button type="button" class="mt-3 btn btn-outline-success btn-rounded w-100" data-mdb-ripple-init>
                Посмотреть на карте
            </button>
        </div>
    </div>
</div>
