<%-- 
    Document   : app
    Created on : Feb 14, 2021, 1:52:35 PM
    Author     : alex
--%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="Application template" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="title" required="true"%>

<%-- any content can be specified here e.g.: --%>
<!DOCTYPE html>
<html lang="ru">
    
    <head>
        <!-- Global site tag (gtag.js) - Google Analytics -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-190622177-1"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
                gtag('js', new Date());

                gtag('config', 'UA-190622177-1');
        </script>

        <meta charset="utf-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">

        <title>${title}</title>
        <meta content="Все для сада и огорода" name="description">
        <meta content="Семена растений, капельный полив, сад и огород, средства защиты растений, удобрения" name="keywords">

        <!-- Favicons -->
        <link href="/favicon.svg?" rel="icon">
        <link href="/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Montserrat:300,400,500,600,700" rel="stylesheet">

        <!-- Vendor CSS Files -->
        <link href="/assets/vendor/aos/aos.css" rel="stylesheet">
        <link href="/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="/assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
        <link href="/assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
        <link href="/assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">
        <link href="/assets/vendor/fontawesome-free-5.15.2-web/css/all.css" rel="stylesheet">

        <!-- Template Main CSS File -->
        <link href="/assets/css/style.css" rel="stylesheet">

        <!-- =======================================================
        * Template Name: Rapid - v4.0.1
        * Template URL: https://bootstrapmade.com/rapid-multipurpose-bootstrap-business-template/
        * Author: BootstrapMade.com
        * License: https://bootstrapmade.com/license/
        ======================================================== -->
    </head>

    <body>
        <t:navbar></t:navbar>
        <jsp:doBody/>
        <t:footer></t:footer>

        <a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

        <!-- Vendor JS Files -->
        <script src="/assets/vendor/aos/aos.js"></script>
        <script src="/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="/assets/vendor/glightbox/js/glightbox.min.js"></script>
        <script src="/assets/vendor/isotope-layout/isotope.pkgd.min.js"></script>
        <script src="/assets/vendor/purecounter/purecounter.js"></script>
        <script src="/assets/vendor/swiper/swiper-bundle.min.js"></script>

        <!-- Template Main JS File -->
        <script src="/assets/js/main.js"></script>

    </body>

</html>