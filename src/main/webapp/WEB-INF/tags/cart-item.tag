<%-- 
    Document   : cart-item
    Created on : Apr 1, 2021, 8:58:45 PM
    Author     : alex
--%>

<%@tag import="java.util.Map"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8" import="ss.entity.agrolavka.*,ss.agrolavka.util.UrlProducer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="position" required="true" type="OrderPosition"%>

<%-- any content can be specified here e.g.: --%>
<div class="card mb-4 shadow-1-strong">
    <div class="card-body">
        <div class="row">

            <c:choose>
                <c:when test="${position.product.images.size() > 0}">
                    <div class="col-md-2 col-lg-1 agr-cart-position-image d-none d-md-block" 
                         style="background-image: url('/media/${position.product.images.get(0).fileNameOnDisk}')" alt="${position.product.name}">
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-md-2 col-lg-1 agr-cart-position-image d-none d-md-block" 
                         style="background-image: url('/assets/img/no-image.png')" alt="${position.product.name}'">
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="col-sm-12 col-md-6 col-lg-8 ps-3 pe-3">
                <a href="<%= UrlProducer.buildProductUrl(position.getProduct())%>" class="agr-link"><h6>${position.product.name}</h6></a>
                <small class="text-muted agr-cart-position-description mb-2">
                    ${position.product.description}
                </small>
            </div>

            <div class="col-sm-12 col-md-4 col-lg-3 d-flex flex-column">
                <div class="d-flex justify-content-end">
                    <button class="btn btn-primary btn-floating me-2" type="button" data-product-quantity-plus>
                        <i class="fas fa-plus"></i>
                    </button>
                    <input type="number" step=".1" class="form-control" aria-label="Количество" min="1" value="${position.quantity}"
                           data-product-position-id="${position.positionId}" data-product-quantity style="flex: 1">
                    <button class="btn btn-primary btn-floating me-2 ms-2" type="button" data-product-quantity-minus>
                        <i class="fas fa-minus"></i>
                    </button>
                    <button class="btn btn-danger btn-floating" data-remove-product-from-cart data-product-position-id="${position.positionId}"
                            type="button">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>

                <div style="flex: 1;" class="d-flex justify-content-end align-items-end">
                    <c:if test="${not empty position.product.discount}">
                        <button class="btn btn-sm btn-danger btn-rounded fs-6 me-2">
                            <i class="fas fa-fire me-2"></i>
                            <%
                                String priceWithDiscount = String.format("%.2f", position.getProduct().getDiscountPrice());
                                String[] priceWithDiscountParts = priceWithDiscount.split("\\.");
                                out.print(priceWithDiscountParts[0] + ".");
                                out.print("<small>" + priceWithDiscountParts[1] + "</small>");
                            %> <small>BYN</small>
                        </button>
                    </c:if>
                    <span class="mt-4 fw-bold ${not empty position.product.discount ? 'text-decoration-line-through text-muted' : 'text-dark'}">
                        <%
                            Double priceVal = position.getPrice();
                            if (position.getProduct() != null && position.getProduct().getVolumes() == null) {
                                priceVal = position.getProduct().getPrice();
                            }
                            String price = String.format("%.2f", priceVal);
                            String[] parts = price.split("\\.");
                            out.print(parts[0] + ".");
                            out.print("<small>" + parts[1] + "</small>");
                        %> <small class="text-muted">BYN</small></span>
                        <%
                            if (position.getProduct().getVolumes() != null) {
                                try {
                                    final Map<Double, String> volumePrices = position.getProduct().getVolumePrices();
                                    final String volume = volumePrices.get(position.getPrice());
                                    if (volume != null) {
                                        out.print("<small class=\"text-muted fw-bold ms-1\">/ ");
                                        out.print(volume);
                                        out.print("</small>");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        %>
                </div>

            </div>

        </div>
    </div>
</div>