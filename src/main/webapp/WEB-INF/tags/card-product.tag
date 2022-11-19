<%-- 
    Document   : product-card
    Created on : Mar 14, 2021, 4:25:15 PM
    Author     : alex
--%>

<%@tag import="org.json.JSONObject"%>
<%@tag import="org.json.JSONArray"%>
<%@tag import="java.text.SimpleDateFormat"%>
<%@tag import="java.util.Objects"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8" 
       import="ss.entity.agrolavka.*,ss.agrolavka.util.UrlProducer"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="product" required="true" type="Product"%>
<%@attribute name="cart" required="true" type="Order"%>
<%@attribute name="noHover" required="false" type="Boolean"%>
<%@attribute name="showCreatedDate" required="false" type="Boolean"%>
<%@attribute name="showAdditionalPhotos" required="false" type="Boolean"%>

<%-- any content can be specified here e.g.: --%>
<a href="<%= UrlProducer.buildProductUrl(product) %>">
    <div class="card shadow-1-strong mb-4 ${noHover ? '' : 'hover-shadow'} ${product.quantity > 0 ? '' : 'agr-product-not-available'}">
        <div class="ribbon ribbon-top-left">
            <span class="${product.quantity > 0 ? 'bg-success' : 'bg-danger'}">
                <small>${product.quantity > 0 ? 'в наличии' : 'под заказ'}</small>
            </span>
        </div>
        <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
            <c:choose>
                <c:when test="${product.images.size() > 0}">
                    <div class="card-img-top agr-card-image ${showAdditionalPhotos ? "agr-clicked-photo" : ""}" style="background-image: url('/media/${product.images.get(0).fileNameOnDisk}?timestamp=${product.images.get(0).createdDate}')"></div>
                </c:when>
                <c:otherwise>
                    <div class="card-img-top agr-card-image" style="background-image: url('/assets/img/no-image.png')"></div>
                </c:otherwise>
            </c:choose>
                    
            <!-- ================================= Additional images =============================================== -->
            <c:if test="${showAdditionalPhotos}">
                <c:if test="${product.images.size() > 1}">
                    <div class="row p-2">
                        <div class="col-4">
                            <c:if test="${product.images.size() > 0}">
                                <div class="agr-product-photos img-thumbnail agr-photo-active" style="background-image: url('/media/${product.images.get(0).fileNameOnDisk}?timestamp=${product.images.get(0).createdDate}')"></div>
                            </c:if>
                        </div>
                        <div class="col-4">
                            <c:if test="${product.images.size() > 1}">
                                <div class="agr-product-photos img-thumbnail" style="background-image: url('/media/${product.images.get(1).fileNameOnDisk}?timestamp=${product.images.get(1).createdDate}')"></div>
                            </c:if>
                        </div>
                        <div class="col-4">
                            <c:if test="${product.images.size() > 2}">
                                <div class="agr-product-photos img-thumbnail" style="background-image: url('/media/${product.images.get(2).fileNameOnDisk}?timestamp=${product.images.get(2).createdDate}')"></div>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </c:if>
            
            <!-- ================================= Additional images =============================================== -->

                <!--div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div-->

            <div class="card-body" style="min-height: 100px;">
                <h6 class="card-title text-dark text-left" style="min-height: 60px;">${product.name}</h6>
                <div class="d-flex align-items-center justify-content-between agr-card-line">
                    <small class="text-muted">Цена</small>
                    <span class="fw-bold ${not empty product.discount ? 'text-decoration-line-through text-muted' : 'text-dark'}">
                        <%
                            String price = String.format("%.2f", product.getPrice());
                            String[] parts = price.split("\\.");
                            out.print(parts[0] + ".");
                            out.print("<small>" + parts[1] + "</small>");
                        %> <small class="text-muted">BYN</small>
                    </span>
                </div>
                <c:if test="${not empty product.discount}">
                    <div class="d-flex align-items-center justify-content-between agr-card-line">
                        <small class="text-danger"><i class="fas fa-fire me-1"></i> Акция</small>
                        <div class="text-danger fw-bold">
                            <%
                                String priceWithDiscount = String.format("%.2f", product.getDiscountPrice());
                                String[] priceWithDiscountParts = priceWithDiscount.split("\\.");
                                out.print(priceWithDiscountParts[0] + ".");
                                out.print("<small>" + priceWithDiscountParts[1] + "</small>");
                            %> <small>BYN</small>
                        </div>
                    </div>
                </c:if>
                
                <c:if test="${showCreatedDate}">
                    <div class="d-flex align-items-center justify-content-between mb-1 agr-card-line">
                        <small class="text-muted">Добавлено</small>
                        <small class="text-muted">
                            <%
                                out.print(new SimpleDateFormat("dd.MM.yyyy").format(product.getCreatedDate()));
                            %>
                        </small>
                    </div>
                </c:if>
                <%
                    boolean inCart = false;
                    for (OrderPosition pos : cart.getPositions()) {
                        if (Objects.equals(product.getId(), pos.getProductId())) {
                            inCart = true;
                            break;
                        }
                    }
                %>
                
                <%
                    if (product.getVolumes() != null) {
                %>
                    <t:product-volumes product="${product}"></t:product-volumes>
                <%
                    } else {
                %>
                <button class="btn btn-outline-info btn-rounded w-100 mt-1 agr-card-button" data-product-id="${product.id}" data-order="" style="z-index: 9000;">
                    <i class="far fa-hand-point-up me-2"></i> Заказать сразу
                </button>
                <%
                    }
                %>
                
                <%
                    if (!inCart) {
                %>
                <button class="btn btn-outline-success btn-rounded w-100 mt-1 agr-card-button" data-product-id="${product.id}" data-add="" style="z-index: 9000;">
                    <i class="fas fa-cart-plus me-2"></i> В корзину
                </button>
                <%
                    } else {
                %>
                <button class="btn btn-outline-danger btn-rounded w-100 mt-1 agr-card-button" data-product-id="${product.id}" data-remove="" style="z-index: 9000;">
                    <i class="fas fa-minus-circle me-2"></i> Из корзины
                </button>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</a>
