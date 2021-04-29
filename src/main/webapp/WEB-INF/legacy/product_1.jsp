<%-- 
    Document   : product
    Created on : Feb 23, 2021, 10:23:56 AM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:app title="${title}" metaDescription="${metaDescription}" canonical="${canonical}">
    
    <jsp:attribute name="structuredData">
        <script type="application/ld+json">
        {
          "@context": "https://schema.org/",
          "@type": "Product",
          "name": "${product.name}",
          "image": [
            "https://agrolavka.by/api/agrolavka/public/product-image/${product.id}"
           ],
          "description": "${product.description}",
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
        <main id="main">
            <section class="services">
                <div class="container">
                    <header class="section-header">
                        <h3>${product.name}</h3>
                        <p class="text-uppercase text-muted">${product.group.name}</p>
                    </header>
                    <div class="row justify-content-center">
                        <div class="col-lg-3 col-md-12 intro-info order-lg-first order-last">
                            <t:product-groups-tree groups="${groups}" groupId="${groupId}"></t:product-groups-tree>
                        </div>

                        <div class="col-lg-9 col-md-12 intro-info order-lg-first order-last">
                            <t:breadcrumb label="${breadcrumbLabel}" groups="${breadcrumbPath}"></t:breadcrumb>
                            <article>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="float-start me-4" style="width: 300px;">
                                            <t:product-card product="${product}" cart="${cart}"/>
                                        </div>
                                        <h4>${product.name}</h4>
                                        <p class="product-text">${product.description}</p>
                                    </div>
                                </div>
                            </article>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    </jsp:body>
    
</t:app>