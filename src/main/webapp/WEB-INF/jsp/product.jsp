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
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@8/swiper-bundle.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/swiper@8/swiper-bundle.min.js"></script>
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
                            <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 d-flex flex-column">
                                <t:product-images product="${product}"></t:product-images>
                                <hr/>
                                <t:product-price product="${product}"></t:product-price>
                                <hr/>
                                <t:product-actions cart="${cart}" product="${product}"></t:product-actions>
                                <hr/>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-md-8 col-lg-9">
                                <t:product-description product="${product}"/>
                            </div>
                        </div>
                    </article>
                </section>
                
            </div>
        </main>
        <t:nav-back-btn></t:nav-back-btn>       
        <modal:one-click-order-modal/>
        <modal:add-to-cart-modal/>
        <modal:photo-modal product="${product}"/>
    </jsp:body>
    
</t:app>
