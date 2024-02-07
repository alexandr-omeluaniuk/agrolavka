<%-- 
    Document   : product
    Created on : Feb 23, 2021, 10:23:56 AM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="modal" tagdir="/WEB-INF/tags/modal" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:app title="${title}" metaDescription="${title}" canonical="${canonical}">
    
    <jsp:attribute name="headSection">
        <link rel="stylesheet" href="/assets/vendor/swiper/swiper-bundle.min.css"/>
        <script src="/assets/vendor/swiper/swiper-bundle.min.js"></script>
    </jsp:attribute>
    
    <jsp:attribute name="structuredData">
        <script type="application/ld+json">
        {
          "@context": "https://schema.org/",
          "@type": "Product",
          "name": "${structuredDataName}",
          "image": [
            "${structuredImage}"
           ],
          "description": "${structuredDataDescription}",
          "offers": {
            "@type": "Offer",
            "url": "${productURL}",
            "priceCurrency": "BYN",
            "price": "${productPrice}",
            "priceValidUntil": "${priceValidUntil}",
            "itemCondition": "https://schema.org/UsedCondition",
            "availability": "https://schema.org/InStock"
          },
          "review": [],
          "aggregateRating": {
            "@type": "AggregateRating",
            "ratingValue": "100",
            "bestRating": "100",
            "ratingCount": "100"
          }
        }
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <main class="min-vh-100">
            <div class="container">
                
                <section>
                    <h3 class="text-center mb-4" id="agr-product-name-title">${product.name}</h3>
                    <h5 class="text-center text-muted text-uppercase mb-4">${product.group.name}</h5>
                    <t:breadcrumb label="${breadcrumbLabel}" groups="${breadcrumbPath}"></t:breadcrumb>
                    <article>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 d-flex flex-column gx-5">
                                <t:product-images product="${product}"></t:product-images>
                                <hr/>
                                <x-agr-product-price 
                                    data-discount="${productDiscount}" 
                                    data-price="${productPrice}"></x-agr-product-price>
                                <hr/>
                                <x-agr-product-actions 
                                    data-id="${product.id}" 
                                    data-in-cart="${inCart ? "true" : ""}" 
                                    data-variants="${variants}"
                                    data-volume="${volumes}"></x-agr-product-actions>
                                <hr/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-8 col-lg-9 gx-5">
                                <x-agr-product-description
                                    data-name="${product.name}"
                                    data-video="${product.videoURL}"
                                    data-description="${fullProductDescription}"
                                    ></x-agr-product-description>
                            </div>
                        </div>
                    </article>
                </section>
                
            </div>
        </main>
        <modal:one-click-order-modal/>
        <modal:add-to-cart-modal/>
        <modal:photo-modal product="${product}"/>
    </jsp:body>
    
</t:app>
