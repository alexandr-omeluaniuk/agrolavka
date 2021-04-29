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
        
        <main class="min-vh-100">
            <div class="container">
                
                <section>
                    <h3 class="text-center mb-4">${product.name}</h3>
                    <h5 class="text-center text-muted text-uppercase mb-4">${product.group.name}</h5>
                    <t:breadcrumb label="${breadcrumbLabel}" groups="${breadcrumbPath}"></t:breadcrumb>
                    <article>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="float-start me-4" style="width: 300px;">
                                    <t:card-product product="${product}" cart="${cart}"/>
                                </div>
                                <h4>${product.name}</h4>
                                <p class="text-justify">${product.description}</p>
                            </div>
                        </div>
                    </article>
                </section>
                
            </div>
        </main>
    </jsp:body>
    
</t:app>
