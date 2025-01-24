<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="POJO.Utilisateur" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("principal");
    if (utilisateur == null || !"Admin".equals(utilisateur.getRole())) {
        response.sendRedirect("/calendrier");
        return;
    }
    List<Utilisateur> utilisateurs = (List<Utilisateur>) request.getAttribute("utilisateurs");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="title.administration"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<a href="/calendrier" class="btn btn-link"><spring:message code="button.calendrier"/></a>
<a href="toutesReservations" class="btn btn-link"><spring:message code="button.toutesReservations"/></a>
<div class="container mt-5">
    <h1 class="mb-4"><spring:message code="title.administration"/></h1>

    <%-- Message de succès si présent --%>
    <% String successMessage = (String) request.getAttribute("successMessage"); %>
    <% if (successMessage != null) { %>
        <div class="alert alert-success">
            <%= successMessage %>
        </div>
    <% } %>

    <%-- Liste des utilisateurs et formulaire de modification --%>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>#</th>
            <th><spring:message code="label.nom"/></th>
            <th><spring:message code="label.prenom"/></th>
            <th><spring:message code="label.email"/></th>
            <th><spring:message code="label.role"/></th>
            <th><spring:message code="label.image"/></th>
            <th><spring:message code="label.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <% if (utilisateurs != null) { %>
            <% for (Utilisateur u : utilisateurs) { %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><%= u.getNom() %></td>
                    <td><%= u.getPrenom() %></td>
                    <td><%= u.getEmail() %></td>
                    <td><%= u.getRole() %></td>
                    <td><img src="/utilisateurs/<%= u.getId() %>/image" alt="X" style="max-width: 200px; height: auto;"/></td>
                    <td>
                        <form action="modifierUtilisateur" method="post" class="d-inline">
                            <input type="hidden" name="id" value="<%= u.getId() %>">
                            <button type="submit" class="btn btn-sm btn-primary"><spring:message code="button.modifier"/></button>
                        </form>
                        <form action="supprimerUtilisateur" method="post" class="d-inline">
                            <input type="hidden" name="id" value="<%= u.getId() %>">
                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?');"><spring:message code="button.supprimer"/></button>
                        </form>
                    </td>
                </tr>
            <% } %>
        <% } else { %>
            <tr>
                <td colspan="6" class="text-center"><spring:message code="label.aucunUtilisateur"/></td>
            </tr>
        <% } %>
        </tbody>
    </table>
    <div class="text-center mt-4">
        <a href="/admin" class="btn btn-primary"><spring:message code="button.retour"/></a>
    </div>
</div>
</body>
</html>
