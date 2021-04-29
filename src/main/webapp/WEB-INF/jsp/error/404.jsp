<%-- 
    Document   : 404
    Created on : Mar 27, 2021, 9:38:33 PM
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<t:app title="Страница не найдена" metaDescription="Страница не найдена или перемещена в другое место" canonical="/error/page-not-found">
    <jsp:body>
        <main class="d-flex justify-content-center align-items-center container min-vh-100">
            <div class="row">
                <div class="col-12">
                    <div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        Упс... Страница не найдена. Возможно она была удалена или перемещена в другое место.<br/><br/>
                        <a href="/" class="agr-link">Вернуться на главную</a>
                    </div>
                </div>
            </div>
        </main>
    </jsp:body>
</t:app>
