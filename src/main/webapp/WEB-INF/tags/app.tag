<%-- 
    Document   : app
    Created on : Feb 14, 2021, 1:52:35 PM
    Author     : alex
--%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="Application template" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="title" required="true"%>
<%@attribute name="metaDescription" required="true"%>
<%@attribute name="canonical" required="true" type="String"%>
<%@attribute name="structuredData" fragment="true" required="false"%>
<%@attribute name="headSection" fragment="true" required="false"%>

<%-- any content can be specified here e.g.: --%>
<!DOCTYPE html>
<html lang="ru">
    
    <head>
        <meta charset="utf-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <!-- Global site tag (gtag.js) - Google Analytics -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-190622177-1"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
                gtag('js', new Date());

                gtag('config', 'UA-190622177-1', { cookie_flags: 'SameSite=None;Secure' });
        </script>
        
        <!-- Google structured data -->
        <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Organization",
          "url": "https://agrolavka.by",
          "logo": "https://agrolavka.by/assets/img/apple-touch-icon.png"
        }
        </script>
        <jsp:invoke fragment="structuredData"/>

        <title>${title}</title>
        <meta name="title" content="Все для сада и огорода">
        <meta name="description" content="${metaDescription}">
        <link rel="canonical" href="https://agrolavka.by${canonical}"/>
        <!-- Open Graph / Facebook -->
        <meta property="og:type" content="website">
        <meta property="og:site_name" content="Агролавка">
        <meta property="og:url" content="https://agrolavka.by">
        <meta property="og:title" content="Все для сада и огорода">
        <meta property="og:description" content="Большой выбор семян, удобрений, средств для защиты растений. Комплектующие для капельного полива. Зоотовары. Приглашаем за покупками.">
        <meta property="og:image" content="https://agrolavka.by/assets/img/agrolavka-location.webp">
        <meta property="og:image:width" content="908">
        <meta property="og:image:height" content="920">
        <meta property="og:locale" content="ru_RU">

        <!-- Twitter -->
        <meta name="twitter:card" content="summary_large_image">
        <meta name="twitter:site" content="insales.ru">
        <meta name="twitter:url" content="https://agrolavka.by">
        <meta name="twitter:title" content="Все для сада и огорода">
        <meta name="twitter:description" content="Большой выбор семян, удобрений, средств для защиты растений. Комплектующие для капельного полива. Зоотовары. Приглашаем за покупками.">
        <meta name="twitter:image" content="https://agrolavka.by/assets/img/agrolavka-location.webp">
        
        <!-- Favicons -->
        <link href="/favicon.svg?" rel="icon">
        <link href="/assets/img/apple-touch-icon.png" rel="apple-touch-icon">
        
        <link rel= "preconnect" href="https://kit.fontawesome.com" />
        <link rel= "preconnect" href="https://fonts.googleapis.com" />
        <link rel= "preconnect" href="https://cdnjs.cloudflare.com" />

        <!-- Vendor CSS Files -->
        <script src="https://kit.fontawesome.com/c4a406170c.js" crossorigin="anonymous" async></script>
        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap" rel="stylesheet"/>
        <!-- MDB -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/3.5.0/mdb.min.css" rel="stylesheet"/>

        <!-- Main CSS File -->
        <link href="/assets/css/style-mb.css?v=27" rel="stylesheet">
        <jsp:invoke fragment="headSection"/>
    </head>

    <body>
        <t:navbar></t:navbar>
        <t:navbar-mobile></t:navbar-mobile>
        <t:sidebar></t:sidebar>
        <jsp:doBody/>
        <t:footer></t:footer>

        <!-- Vendor JS Files -->
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/3.5.0/mdb.min.js"></script>
        <script type="text/javascript" src="/assets/vendor/purecounter/purecounter.js"></script>
        <!-- Main CSS File -->
        <script type="module" src="/assets/js/main-mb.js?v=18"></script>
        <script type="module" src="/assets/js/modules/product-card.js?v=1"></script>
        <script type="module" src="/assets/js/modules/cart.js?v=1"></script>
    </body>

</html>