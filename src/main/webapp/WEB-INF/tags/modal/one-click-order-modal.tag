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
                    <div class="input-group">
                        <input type="number" class="form-control" placeholder="Количество" name="quantity" min="1" required aria-describedby="quantity-type-alt">
                        <span class="input-group-text border-0" id="quantity-type-alt">шт.</span>
                    </div>
                    <input name="productId" hidden required>
                    <!--input name="volumePrice" hidden-->
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

<div class="toast fade text-white bg-success" role="alert" aria-live="assertive" aria-atomic="true" data-mdb-color="success" data-mdb-autohide="false"
     style="position: fixed; top: 70px; right: 20px; z-index: 9999; display: none;" id="agr-one-click-order-success">
    <div class="toast-header text-white bg-success">
        <i class="fas fa-check fa-lg me-2"></i>
        <strong class="me-auto">Спасибо за Ваш заказ</strong>
        <button type="button" class="btn-close btn-close-white" data-mdb-dismiss="toast" aria-label="Close"></button>
    </div>
    <div class="toast-body">Мы Вам обязательно перезвоним</div>
</div>