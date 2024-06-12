<%-- 
    Document   : quick-search-mobile
    Created on : Feb 23, 2021, 7:16:20 PM
    Author     : alex
--%>

<%@tag description="Quick search mobile" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%-- any content can be specified here e.g.: --%>
<div class="input-group me-2" id="agr-quick-search-container-mobile-trigger">
    <input type="search" class="form-control" aria-label="Поиск товаров" id="agr-quick-search-input-mobile-trigger" placeholder="Быстрый поиск"
           autocomplete="off">
    <span class="input-group-text">
        <i class="fas fa-search" style="color:white;" data-mdb-toggle="modal" data-mdb-target="#agr-quick-search-mobile-modal"></i>
    </span>
</div>
<div class="modal fade" tabindex="-1" id="agr-quick-search-mobile-modal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div class="position-relative me-2 w-100">
                    <input type="text" class="form-control modal-title"
                        aria-label="Поиск товаров" id="agr-quick-search-input-mobile"
                        placeholder="Быстрый поиск" autocomplete="off" tabindex="1">
                    <i class="fas fa-times position-absolute d-none" style="right: 10px; top: 10px;" id="agr-quick-search-input-mobile-clear"></i>
                </div>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <ul class="list-group" id="agr-quick-search-result-mobile"></ul>
            </div>
        </div>
    </div>
</div>