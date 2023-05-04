<%-- 
    Document   : top-product-groups
    Created on : Mar 17, 2021, 7:59:50 PM
    Author     : alex
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%@attribute name="totalInteger" required="true" type="Integer"%>
<%@attribute name="totalDecimal" required="true" type="Integer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>

<%-- any content can be specified here e.g.: --%>
<div class="card shadow-1-strong">
    <div class="card-body p-3">
        <div class="d-flex justify-content-between">
            <span class="text-muted">Товаров на</span>
            <div>
                <span class="text-dark fw-bold me-2">
                    ${totalInteger}.<small>${totalDecimal}</small>
                    <small class="text-muted">BYN</small>
                </span>
            </div>
        </div>
        <!--hr/>
        <div class="d-flex justify-content-between">
            <span class="text-muted">Доставка</span>
            <div>
                <span class="text-dark fw-bold me-2">
                    0.<small>00</small>
                    <small class="text-muted">BYN</small>
                </span>
            </div>
        </div-->
        <hr/>
        <div class="d-flex justify-content-between">
            <span class="text-dark">Итого</span>
            <div>
                <span class="text-dark fw-bold me-2">
                    ${totalInteger}.<small>${totalDecimal}</small>
                    <small class="text-muted">BYN</small>
                </span>
            </div>
        </div>
    </div>
</div>