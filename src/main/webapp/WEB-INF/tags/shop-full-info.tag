<%-- 
    Document   : breadcrumb
    Created on : Feb 24, 2021, 11:45:26 AM
    Author     : alex
--%>

<%@tag import="ss.entity.martin.EntityImage"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.Shop,ss.entity.martin.EntityImage"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="shop" required="true" type="ss.entity.agrolavka.Shop"%>

<%-- any content can be specified here e.g.: --%>
<c:if test="${shop.images.size() > 0}">
    <section class="mb-2">
        <div class="row">
            <div class="col-md-6 gx-5 mb-4">
                <img src="/media/${shop.images.get(0).fileNameOnDisk}?timestamp=${shop.images.get(0).createdDate}"
                     class="img-fluid shadow-2-strong" style="width: 100%"
                     alt="${shop.title}"/>
            </div>
            <div class="col-md-6 gx-5 mb-4">
                <h5>${shop.title}</h5>
                <p style="white-space: pre-line;">${shop.description}</p>
                <h6>Адрес</h6>
                <p style="white-space: pre-line;">${shop.address}</p>
                <h6>Время работы</h6>
                <%
                    final String workingWhours = shop.getWorkingHours();
                    final String[] lines = workingWhours.split("\n");
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
            </div>
        </div>
        <div class="row">
            <%
                for (int i = 1; i < 5; i++) {
                    if (shop.getImages().size() > i) {
                        final EntityImage image = shop.getImages().get(i);
                        out.print("<div class=\"col-lg col-md-6 col-sm-6 col-6 gx-5\">");
                        out.print("<img src=\"/media/" + image.getFileNameOnDisk() + "?timestamp=" 
                            + image.getCreatedDate() + "\" class=\"img-fluid shadow-2-strong\" style=\"width: 100%\" alt=\"" + image.getName() + "\"/>");
                        out.print("</div>");
                    } else {
                        out.print("<div class=\"col-lg col-md-6 col-sm-6 col-6 gx-5\">");
                        out.print("</div>");
                    }
                }
            %>
        </div>
    </section>
    <hr/>
</c:if>
