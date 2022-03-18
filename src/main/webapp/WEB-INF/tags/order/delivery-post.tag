<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<div class="mt-3 d-none" id="order-address">
    <small class="text-muted mb-2 ms-2">* фамилия имя и отчество заполняются только если планируется доставка почтой</small>
    <div class="row">
        <div class="col-sm-12 col-lg-4">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="lastname" placeholder="Иванов"
                       name="lastname">
                <label for="lastname">Фамилия</label>
            </div>
        </div>
        <div class="col-sm-12 col-lg-4">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="firstname" placeholder="Иван"
                       name="firstname">
                <label for="firstname">Имя</label>
            </div>
        </div>
        <div class="col-sm-12 col-lg-4">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="middlename" placeholder="Иванович"
                       name="middlename">
                <label for="middlename">Отчество</label>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="address-region" placeholder="Брестская" required
                       readonly name="region">
                <label for="address-region">Область *</label>
            </div>
        </div>
        <div class="col-sm-12 col-md-6">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="address-district" placeholder="Кобринский"
                       readonly required name="district">
                <label for="address-district">Район *</label>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12 col-md-9">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="address-city" placeholder="Брест" required
                       readonly name="city">
                <label for="address-city">Населенный пункт *</label>
            </div>
        </div>
        <div class="col-sm-12 col-md-3">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="address-postcode" placeholder="224033"
                       name="postcode">
                <label for="address-postcode">Почтовый индекс</label>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="address-street" placeholder="Рябиновая" required
                       readonly name="street">
                <label for="address-street">Улица *</label>
            </div>
        </div>
        <div class="col-sm-12 col-md-3">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="address-house" placeholder="31" required
                       readonly name="house">
                <label for="address-house">Дом *</label>
            </div>
        </div>
        <div class="col-sm-12 col-md-3">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="address-flat" placeholder="5555" name="flat">
                <label for="address-flat">Квартира</label>
            </div>
        </div>
    </div>
</div>