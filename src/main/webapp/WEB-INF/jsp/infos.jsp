<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="POJO.Utilisateur" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("principal");
    if (utilisateur == null) {
        response.sendRedirect("connexion");
        return;
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="title.infosPersonnelles"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<a href="calendrier" class="btn btn-link"><spring:message code="link.calendrier"/></a>
<a href="mesReservations" class="btn btn-link"><spring:message code="link.mesReservations"/></a>
<a href="deconnexion" class="btn btn-secondary"><spring:message code="link.deconnexion"/></a>
<div class="container mt-5">
    <h1 class="mb-4"><spring:message code="header.mesInfosPersonnelles"/></h1>

    <%-- Message de succès si présent --%>
    <% String successMessage = (String) request.getAttribute("successMessage"); %>
    <% if (successMessage != null) { %>
        <div class="alert alert-success">
            <%= successMessage %>
        </div>
    <% } %>

    <%-- Formulaire pour afficher et modifier les informations --%>
    <form action="modifierInfos" method="post" enctype="multipart/form-data">
        <div class="mb-3">
            <label for="nom" class="form-label"><spring:message code="label.nom"/></label>
            <input type="text" id="nom" name="nom" class="form-control" value="<%= utilisateur.getNom() %>" required>
        </div>
        <div class="mb-3">
            <label for="prenom" class="form-label"><spring:message code="label.prenom"/></label>
            <input type="text" id="prenom" name="prenom" class="form-control" value="<%= utilisateur.getPrenom() %>" required>
        </div>
        <div class="mb-3">
            <label for="mdp" class="form-label"><spring:message code="label.mdp"/></label>
            <input type="password" id="mdp" name="mdp" class="form-control">
        </div>
        <div class="mb-3">
            <img src="/utilisateurs/${utilisateur.id}/image" style="max-width: 200px; height: auto;"/>
            <label for="image" class="form-label"><spring:message code="label.image"/></label>
            <input type="file" id="image" name="image" class="form-control" accept="image/*">
        </div>
        <button type="submit" class="btn btn-primary"><spring:message code="button.enregistrer"/></button>
    </form>
</div>
</body>
</html>
