<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<div class="modal fade" id="agr-add-to-cart-modal" tabindex="-1" aria-labelledby="agr-add-to-cart-modal-header" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="agr-add-to-cart-modal-header">
                    Пожалуйста, укажите количество товара
                </h5>
            </div>
            <div class="modal-body">
                <form>
                    <div class="input-group">
                        <input type="number" class="form-control" placeholder="Количество" name="quantity" min="1" required aria-describedby="quantity-type">
                        <span class="input-group-text" id="quantity-type">шт.</span>
                    </div>
                    <input name="productId" hidden required>
                </form>
            </div>
            <div class="modal-footer d-flex justify-content-between">
                <button type="button" class="btn btn-light" data-mdb-dismiss="modal">Закрыть</button>
                <button type="button" class="btn btn-primary" data-add-to-cart-confirm>
                    Добавить в корзину
                </button>
            </div>
        </div>
    </div>
</div>