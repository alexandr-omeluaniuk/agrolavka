<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<fieldset>
    <legend class="col-form-label pt-0 fw-bolder mb-2">
        Контактные данные
    </legend>
    <label for="order-mobile" class="mb-1">Номер мобильного телефона</label>
    <div class="input-group mb-3">
        <span class="input-group-text"><i class="fa fa-phone-alt"></i></span>
        <input type="text" class="form-control" placeholder="Номер телефона" required
           id="order-mobile" name="phone">
    </div>

    <div class="mb-3">
        <label for="comment">Комментарий к заказу</label>
        <textarea type="text" class="form-control" id="comment" name="comment"></textarea>
    </div>

</fieldset>