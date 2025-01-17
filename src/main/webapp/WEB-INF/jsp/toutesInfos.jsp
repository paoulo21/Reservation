<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="POJO.Utilisateur" %>
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
    <title>Administration - Gestion des utilisateurs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<a href="/calendrier" class="btn btn-link">Calendrier</a>
<a href="toutesReservations" class="btn btn-link">Toutes les Réservations</a>
<div class="container mt-5">
    <h1 class="mb-4">Gestion des utilisateurs</h1>

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
            <th>Nom</th>
            <th>Prénom</th>
            <th>Email</th>
            <th>Rôle</th>
            <th>Image</th>
            <th>Actions</th>
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
                    <td><img src="/utilisateurs/<%= u.getId() %>/image" alt="Image de Profil" style="max-width: 200px; height: auto;"/></td>
                    <td>
                        <form action="modifierUtilisateur" method="post" class="d-inline">
                            <input type="hidden" name="id" value="<%= u.getId() %>">
                            <button type="submit" class="btn btn-sm btn-primary">Modifier</button>
                        </form>
                        <form action="supprimerUtilisateur" method="post" class="d-inline">
                            <input type="hidden" name="id" value="<%= u.getId() %>">
                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?');">Supprimer</button>
                        </form>
                    </td>
                </tr>
            <% } %>
        <% } else { %>
            <tr>
                <td colspan="6" class="text-center">Aucun utilisateur trouvé</td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
