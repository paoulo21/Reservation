<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="POJO.Constraints" %>
<%@ page import="POJO.Utilisateur" %>
<%
    Constraints constraints = (Constraints) request.getAttribute("constraints");
    LocalDate jour = (LocalDate) request.getAttribute("jour");
    Map<String, Integer> creneauxComplets = (Map<String, Integer>) request.getAttribute("creneauxComplets");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservations pour le <%= jour %></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <% 
        Utilisateur principal = (Utilisateur)session.getAttribute("principal"); 
        %>
        <% if (principal == null) { %>
    <a href="connexion" class="btn btn-primary">Se connecter</a>
    <a href="inscription" class="btn btn-secondary">Créer un compte</a>
<% } else { %>
    <p>
        Bienvenue, <%= principal.getNom()%>.
        <a href="infos" class="btn btn-link">Mes informations</a>
        <a href="mesReservations" class="btn btn-link">Mes Reservations</a>
        <a href="deconnexion" class="btn btn-secondary">Se déconnecter</a>
        <% if (principal.getRole() == "Admin") { %>
            <a href="admin" class="btn btn-link">Panneau Administrateur</a>
        <% } %>
        <img src="/utilisateurs/${principal.id}/image" alt="Image de Profil" style="max-width: 200px; height: auto;"/>
    </p>
<% } %>
    <div class="container my-5">
        <h1 class="text-center mb-4">Calendrier des Créneaux du <%= jour.toString() %></h1>
        <table class="table table-striped table-bordered text-center">
            <thead class="table-dark">
                <tr>
                    <th>Créneau</th>
                    <th>Réservations</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Map.Entry<String, Integer> entry : creneauxComplets.entrySet()) {
                        String creneau = entry.getKey();
                        int reservations = entry.getValue();
                %>
                <tr>
                    <td><%= creneau %></td>
                    <td><%= reservations %> / <%= constraints.getMaxPerSlot() %></td>
                    <td>
                        <% if (reservations < constraints.getMaxPerSlot()) { %>
                            <form method="post" action="ajouterReservation">
                                <input type="hidden" name="jour" value="<%= jour.toString() %>">
                                <input type="hidden" name="creneau" value="<%= creneau %>">
                                <button type="submit" class="btn btn-primary">Réserver</button>
                            </form>
                        <% } else { %>
                            <span class="badge bg-danger">Complet</span>
                        <% } %>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <div class="text-center mt-4">
            <a href="calendrier" class="btn btn-secondary">Retour au calendrier</a>
        </div>
    </div>
    <!-- Bootstrap JS and dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
