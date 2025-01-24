<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="title.admin"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
    <a href="/calendrier" class="btn btn-link"><spring:message code="button.calendrier"/></a>
        <h1 class="mb-4"><spring:message code="title.admin"/></h1>

        <div class="list-group">
            <!-- Lien vers la page Toutes Informations -->
            <a href="/admin/toutesInfos" class="list-group-item list-group-item-action">
                <spring:message code="link.gestionInfos"/>
            </a>

            <!-- Lien vers la page Toutes Réservations -->
            <a href="/admin/toutesReservations" class="list-group-item list-group-item-action">
                <spring:message code="link.gestionReservations"/>
            </a>
        </div>
    </div>
</body>
</html>
