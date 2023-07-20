<%-- 
    Document   : products-search
    Created on : Feb 23, 2021, 7:16:20 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<div class="input-group me-4 d-lg-none d-xl-flex" id="agr-quick-search-container-desktop">
    <input type="search" class="form-control" aria-label="Поиск товаров" id="agr-quick-search-input-desktop" placeholder="Быстрый поиск"
           autocomplete="off">
    <ul class="dropdown-menu" aria-labelledby="agr-quick-search-container-desktop" id="agr-quick-search-result-desktop"></ul>
    <span class="input-group-text"><i class="fas fa-search" style="color:white;"></i></span>
</div>

<button type="button" class="btn btn-outline-light d-none d-lg-block d-xl-none btn-floating me-4" id="agr-quick-search-switcher-desktop">
    <i class="fas fa-search"></i>
</button>
