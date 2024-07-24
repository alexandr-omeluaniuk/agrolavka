<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.agrolavka.constants.SiteConstants"%>
<%@attribute name="totalInteger" required="true" type="Integer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<legend class="col-form-label pt-0 fw-bolder mb-2">Доставка</legend>
<div class="form-check mb-3">
    <input class="form-check-input" type="radio" name="delivery" id="self-delivery" checked>
    <label class="form-check-label" for="self-delivery">
        Самовывоз из магазина Агролавка
    </label>
</div>
<div class="form-check mb-3">
    <input class="form-check-input" type="radio" name="delivery" id="remote-delivery">
    <label class="form-check-label" for="remote-delivery">
        Доставка Белпочтой
    </label>
</div>
<div class="form-check">
    <input class="form-check-input" type="radio" name="delivery" id="europost-delivery">
    <label class="form-check-label" for="europost-delivery">
        Доставка <a href="https://evropochta.by" target="_blank">Европочтой</a>
    </label>
</div>
<hr/>