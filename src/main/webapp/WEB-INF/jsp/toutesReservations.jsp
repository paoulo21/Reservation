<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="POJO.Reservation" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Toutes les Réservations</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<a href="/calendrier" class="btn btn-link">Calendrier</a>
<a href="toutesInfos" class="btn btn-link">Tout les Utilisateurs</a>
<div class="container mt-5">
    <h1 class="text-center">Toutes les Réservations</h1>

    <table class="table table-bordered table-striped mt-4">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Utilisateur</th>
            <th>Date</th>
            <th>Créneau</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% 
            // Récupérer la liste des réservations depuis la requête
            List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
            if (reservations != null && !reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
        %>
        <tr>
            <td><%= reservation.getId() %></td>
            <td><%= reservation.getUtilisateur().getNom() + " " + reservation.getUtilisateur().getPrenom() %></td>
            <td><%= reservation.getDateHeure().toLocalDate() %></td>
            <td><%= reservation.getDateHeure().toLocalTime() %></td>
            <td>
                <a href="/admin/supprimerReservation?id=<%= reservation.getId() %>" class="btn btn-danger btn-sm"
                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette réservation ?');">Supprimer</a>
            </td>
        </tr>
        <% 
                }
            } else { 
        %>
        <tr>
            <td colspan="5" class="text-center">Aucune réservation trouvée.</td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <div class="text-center mt-4">
        <a href="/admin" class="btn btn-primary">Retour au tableau de bord</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
