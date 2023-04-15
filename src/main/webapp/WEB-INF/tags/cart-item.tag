<%-- 
    Document   : cart-item
    Created on : Apr 1, 2021, 8:58:45 PM
    Author     : alex
--%>

<%@tag import="java.util.Map"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.entity.agrolavka.*,ss.agrolavka.util.UrlProducer"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="position" required="true" type="OrderPosition"%>

<%-- any content can be specified here e.g.: --%>
<div class="card mb-4 shadow-1-strong" data-cart-item-id="${position.positionId}">
    <div class="card-body">
        <div class="row ps-md-2">

            <c:choose>
                <c:when test="${position.product.images.size() > 0}">
                    <div class="col-lg-1 agr-cart-position-image d-none d-lg-block shadow-1-strong rounded" 
                         style="background-image: url('/media/${position.product.images.get(0).fileNameOnDisk}')" alt="${position.product.name}">
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-lg-1 agr-cart-position-image d-none d-lg-block shadow-1-strong rounded" 
                         style="background-image: url('/assets/img/no-image.png')" alt="${position.product.name}'">
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="col-sm-12 col-md-6 col-lg-6 col-xl-7 ps-3 pe-3">
                <a href="<%= UrlProducer.buildProductUrl(position.getProduct())%>" class="agr-link"><h6>${position.getProductName()}</h6></a>
                <small class="text-muted agr-cart-position-description mb-2">
                    ${position.product.description}
                </small>
            </div>

            <div class="col-sm-12 col-md-6 col-lg-5 col-xl-4 d-flex flex-column">
                <div class="d-flex justify-content-end">
                    <div class="me-4 d-flex rounded-pill shadow-sm p-2">
                        <button class="btn btn-primary btn-floating me-2" type="button" data-product-quantity-plus>
                            <i class="fas fa-plus"></i>
                        </button>
                        <div class="input-group" style="flex: 1;">
                            <input type="number" step="1" class="form-control" aria-label="Количество" min="1" value="${position.quantity}"
                                   data-product-position-id="${position.positionId}" data-product-quantity >
                            <span class="input-group-text border-0">ед.</span>
                        </div>
                        <button class="btn btn-primary btn-floating" type="button" data-product-quantity-minus>
                            <i class="fas fa-minus"></i>
                        </button>
                    </div>
                    <div class="d-flex rounded-pill shadow-sm p-2">
                        <button class="btn btn-danger btn-floating" data-remove-product-from-cart data-product-position-id="${position.positionId}"
                                type="button">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>

                <t:cart-item-info position="${position}"/>

            </div>

        </div>
    </div>
</div>