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
        <div class="container">
            <div class="row">
                <div class="col-12 d-flex flex-column min-vh-100">
                    <!--div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        Упс... Страница не найдена. Возможно она была удалена или перемещена в другое место.<br/><br/>
                        <a href="/" class="agr-link">Вернуться на главную</a>
                    </div-->
                    <div class="d-flex justify-content-center align-items-center" style="flex: 1;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="96" height="96" fill="#000000b8" class="bi bi-compass" viewBox="0 0 16 16">
                          <path d="M8 16.016a7.5 7.5 0 0 0 1.962-14.74A1 1 0 0 0 9 0H7a1 1 0 0 0-.962 1.276A7.5 7.5 0 0 0 8 16.016zm6.5-7.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z"/>
                          <path d="m6.94 7.44 4.95-2.83-2.83 4.95-4.949 2.83 2.828-4.95z"/>
                        </svg>
                    </div>
                    <div style="flex: 1;">
                        <h2 style="text-align: center">Страница не найдена</h2>
                        <hr/>
                        <h6>Упс... Страница не найдена. Возможно она была удалена или перемещена в другое место.</h6>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:app>
