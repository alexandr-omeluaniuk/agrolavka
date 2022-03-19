<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="europostLocations" required="true" type="java.util.List<ss.entity.agrolavka.EuropostLocation>"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<div class="mt-3 d-none" id="order-europost">
    <div class="input-group me-4 d-lg-none d-xl-flex">
        <span class="input-group-text"><i class="fas fa-mail-bulk"></i></span>
        <input type="search" class="form-control" aria-label="Отделения Европочты" placeholder="Выберите отделение Европочты"
               autocomplete="off" id="agr-europost-location-input" name="europostLocationName">
        <ul class="dropdown-menu w-100" aria-labelledby="agr-europost-locations-list" id="agr-europost-locations-list"
                style="overflow-y: auto; max-height: 500px;">
            <c:forEach items="${europostLocations}" var="location">
                <li class="agr-product-search-link">
                    <a class="dropdown-item" href="#">
                        <div class="d-flex w-100 justify-content-between">
                            <h6>${location.city}</h6>
                            <small class="text-muted">${location.address}</small>
                        </div>
                        <div class="d-flex justify-content-between">
                            <small style="white-space: pre-wrap;">${location.workingHours}</small>
                        </div>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<script>
    (function () {
        "use strict";
        
        const input = document.querySelector('#agr-europost-location-input');
        
        input.addEventListener('focus', function (e) {
            const searchResultOutput = document.querySelector('#agr-europost-locations-list');
            if (searchResultOutput.innerHTML) {
                searchResultOutput.classList.add("show");
                searchResultOutput.classList.add("list-group");
            }
        });
        
    })();
</script>