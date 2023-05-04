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
        <div class="row">

            <div class="col-12">
                <a href="<%= UrlProducer.buildProductUrl(position.getProduct())%>" class="agr-link" style="text-decoration: underline"><h5>${position.getProductName()}</h5></a>
            </div>

            <div class="col-12 col-sm-12 col-md-7 col-lg-7 col-xl-8 d-flex mb-2">
                <c:choose>
                    <c:when test="${position.product.images.size() > 0}">
                        <div class="agr-cart-position-image shadow-1-strong rounded-circle" 
                                 style="background-image: url('/media/${position.product.images.get(0).fileNameOnDisk}')" 
                                 alt="${position.product.name}"></div>
                    </c:when>
                    <c:otherwise>
                        <div class="agr-cart-position-image shadow-1-strong rounded-circle" 
                                 style="background-image: url('/assets/img/no-image.png')" 
                                 alt="${position.product.name}"></div>
                    </c:otherwise>
                </c:choose>
                <div class="ms-3" style="flex: 1;">
                    <small class="text-muted agr-cart-position-description">
                        <c:if test="${not empty position.product.description}">
                            ${position.product.description}
                        </c:if>
                        <c:if test="${empty position.product.description}">
                            Нет описания товара
                        </c:if>
                    </small>
                </div>
            </div>

            <div class="col-12 col-sm-12 col-md-5 col-lg-5 col-xl-4 d-flex flex-column">
                <div class="d-flex justify-content-end">
                    <div class="me-4 d-flex rounded-pill shadow-sm p-2 w-100">
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