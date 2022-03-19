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
    <div class="input-group me-4 d-lg-none d-xl-flex" id="agr-europost-location-dropdown">
        <span class="input-group-text"><i class="fas fa-mail-bulk"></i></span>
        <input type="search" class="form-control" aria-label="Отделения Европочты" placeholder="Выберите отделение Европочты"
               autocomplete="chrome-off" id="agr-europost-location-input" name="europostLocationName" required
               style="border-top-right-radius: 0.25rem; border-bottom-right-radius: 0.25rem;">
        <ul class="dropdown-menu w-100" aria-labelledby="agr-europost-locations-list" id="agr-europost-locations-list"
                style="overflow-y: auto; max-height: 500px;">
            <c:forEach items="${europostLocations}" var="location">
                <li class="agr-product-search-link" data-location-address="${location.address}" data-location-id="${location.id}">
                    <a class="dropdown-item" href="#">
                        <div class="d-flex w-100 justify-content-between">
                            <h6>${location.address}</h6>
                            <small class="text-muted">${location.city}</small>
                        </div>
                        <div class="d-flex justify-content-between">
                            <small style="white-space: pre-wrap;">${location.workingHours}</small>
                        </div>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
    <input type="number" hidden="true" name="europostLocationId" required readonly/>
</div>

<script>
    (function () {
        "use strict";
        
        function highlightText(text, searchText) {
            const idx = text.toLowerCase().indexOf(searchText.toLowerCase());
            if (searchText.length > 0 && idx !== -1) {
                return text.substring(0, idx) + '<span class="highlighted-text">' +
                    text.substring(idx, idx + searchText.length) + '</span>' + text.substring(idx + searchText.length);
            } else {
                return text;
            }
        }
        
        const component = document.querySelector('#order-europost');
        const dropdown = component.querySelector('#agr-europost-location-dropdown');
        const input = component.querySelector('#agr-europost-location-input');
        const searchResultOutput = component.querySelector('#agr-europost-locations-list');
        const locationIdInput = component.querySelector('input[name="europostLocationId"]');
        
        input.addEventListener('focus', function (e) {
            if (searchResultOutput.innerHTML) {
                searchResultOutput.classList.add("show");
                searchResultOutput.classList.add("list-group");
            }
        });
        
        dropdown.addEventListener('blur', function (e) {
            if (e.relatedTarget === null) {
                const searchResultOutput = document.querySelector('#agr-europost-locations-list');
                searchResultOutput.classList.remove("show");
                searchResultOutput.classList.remove("list-group");
            }
        }, true);
        
        input.addEventListener('input', function (e) {
            const searchText = this.value;
            searchResultOutput.querySelectorAll('li').forEach(item => {
                const val = item.getAttribute('data-location-address');
                const h6 = item.querySelector('h6');
                if (searchText) {
                    if (val.toLowerCase().indexOf(searchText.toLowerCase()) === -1) {
                        item.classList.add('d-none');
                        h6.innerHTML = val;
                    } else {
                        h6.innerHTML = highlightText(val, searchText);
                    }
                } else {
                    h6.innerHTML = val;
                    item.classList.remove('d-none');
                }
            });
        });
        
        searchResultOutput.addEventListener('click', function (e) {
            e.preventDefault();
            const li = e.target.closest('li[data-location-address]');
            if (li) {
                const id = li.getAttribute('data-location-id');
                locationIdInput.value = id;
                const address = li.getAttribute('data-location-address');
                input.value = address;
                searchResultOutput.classList.remove("show");
                searchResultOutput.classList.remove("list-group");
            }
        });
        
    })();
</script>