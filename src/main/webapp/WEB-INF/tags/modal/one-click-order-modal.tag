<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<div class="modal fade" id="agr-one-click-order-modal" tabindex="-1" aria-labelledby="agr-one-click-order-modal-header" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="agr-one-click-order-modal-header">
                    Пожалуйста, введите номер телефона, мы Вам перезвоним для оформления заказа
                </h5>
            </div>
            <div class="modal-body">
                <form>
                    <div class="input-group mb-3">
                        <span class="input-group-text"><i class="fa fa-phone-alt"></i></span>
                        <input type="text" class="form-control" placeholder="Номер телефона" required
                           id="order-mobile" name="phone">
                    </div>
                    <input name="productId" hidden required>
                </form>
            </div>
            <div class="modal-footer d-flex justify-content-between">
                <button type="button" class="btn btn-light" data-mdb-dismiss="modal">Закрыть</button>
                <button type="button" class="btn btn-primary" data-one-click-order>
                    <div class="spinner-border spinner-border-sm me-1 d-none" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    Заказать
                </button>
            </div>
        </div>
    </div>
</div>