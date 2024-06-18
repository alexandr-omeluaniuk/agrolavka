<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8"
       import="ss.entity.agrolavka.*"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="attributes" required="true" type="java.util.List<ProductAttribute>"%>

<c:forEach items="${attributes}" var="attribute">
    <div class="d-flex flex-column">
        <hr/>
        <h6>
            ${attribute.name}
        </h6>
        <c:forEach items="${attribute.items}" var="item">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="attr-${item.id}">
                <label class="form-check-label" for="attr-${item.id}">
                    ${item.name}
                </label>
            </div>
        </c:forEach>
    </div>
</c:forEach>