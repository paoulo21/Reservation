<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Creneaux.Constraints" %>
<%@ page import="DAO.CalendrierDAO" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%
    // Charger les contraintes et autres informations nécessaires
    Constraints constraints = (Constraints) request.getAttribute("constraints");
    LocalDate dateCourante = (LocalDate) request.getAttribute("jour");

    // Appel de la méthode chargerCreneauxParJour
    CalendrierDAO dao = new CalendrierDAO(); // Remplacez par l'instance appropriée
    Map<String, Integer> reservationsParCreneau = dao.chargerCreneauxParJour(dateCourante, constraints.getMinutesBetweenSlots());

    // Création des créneaux horaires pour la journée complète
    LocalTime start = constraints.getStart();
    LocalTime end = constraints.getEnd();
    int minutesEntreCreneaux = constraints.getMinutesBetweenSlots();
    Map<String, Integer> creneauxComplets = new LinkedHashMap<>();

    while (!start.isAfter(end)) {
        LocalTime nextSlot = start.plusMinutes(minutesEntreCreneaux);
        String creneau = start + "-" + nextSlot;
        creneauxComplets.put(creneau, reservationsParCreneau.getOrDefault(creneau, 0)); // Met à 0 si aucune réservation
        start = nextSlot;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservations pour le <%= dateCourante %></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container my-5">
        <h1 class="text-center mb-4">Calendrier des Créneaux</h1>
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
                            <form method="post" action="ajouterReservationServlet" class="d-inline">
                                <input type="hidden" name="jour" value="<%= dateCourante.toString() %>">
                                <input type="hidden" name="creneau" value="<%= creneau %>">
                                <button type="submit" class="btn btn-primary">Réserver</button>
                            </form>
                        <% } else { %>
                            <span class="badge bg-danger">Complet</span>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <div class="text-center mt-4">
            <a href="calendrier.jsp" class="btn btn-secondary">Retour au calendrier</a>
        </div>
    </div>
    <!-- Bootstrap JS and dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
