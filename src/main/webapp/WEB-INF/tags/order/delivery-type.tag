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
<div class="form-check">
    <input class="form-check-input" type="radio" name="delivery" id="self-delivery" checked>
    <label class="form-check-label" for="self-delivery">
        Самовывоз из магазина Агролавка
    </label>
</div>
<%
    if (totalInteger >= SiteConstants.MIN_ORDER_SUM) {
%>
<div class="form-check">
    <input class="form-check-input" type="radio" name="delivery" id="remote-delivery">
    <label class="form-check-label" for="remote-delivery">
        Доставка по адресу
    </label>
</div>
<div class="form-check">
    <input class="form-check-input" type="radio" name="delivery" id="europost-delivery">
    <label class="form-check-label" for="europost-delivery">
        Доставка <a href="https://evropochta.by" target="_blank">Европочтой</a>
    </label>
</div>
<%
    } else {
%>
<hr/>
<p class="text-muted">Другие способы доставки доступны только если сумма заказа больше <b>${SiteConstants.MIN_ORDER_SUM} рублей</b><br/>
    Подробнее об условиях доставки читайте <a href="/delivery">тут</a>
</p>
<%
    }
%>