<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="POJO.Utilisateur" %>
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
    <title>Infos personnelles</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<a href="calendrier" class="btn btn-link">Calendrier</a>
<a href="mesRendezVous" class="btn btn-link">Mes rendez-vous</a>
<div class="container mt-5">
    <h1 class="mb-4">Mes informations personnelles</h1>

    <%-- Message de succès si présent --%>
    <% String successMessage = (String) request.getAttribute("successMessage"); %>
    <% if (successMessage != null) { %>
        <div class="alert alert-success">
            <%= successMessage %>
        </div>
    <% } %>

    <%-- Formulaire pour afficher et modifier les informations --%>
    <form action="modifierInfos" method="post">
        <div class="mb-3">
            <label for="nom" class="form-label">Nom</label>
            <input type="text" id="nom" name="nom" class="form-control" value="<%= utilisateur.getNom() %>" required>
        </div>
        <div class="mb-3">
            <label for="prenom" class="form-label">Prénom</label>
            <input type="text" id="prenom" name="prenom" class="form-control" value="<%= utilisateur.getPrenom() %>" required>
        </div>
        <div class="mb-3">
            <label for="mdp" class="form-label">Nouveau mot de passe (laisser vide pour ne pas changer)</label>
            <input type="password" id="mdp" name="mdp" class="form-control">
        </div>
        <button type="submit" class="btn btn-primary">Enregistrer les modifications</button>
    </form>
</div>
</body>
</html>
