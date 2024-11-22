<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Creneaux.Constraints" %>
<%
    Constraints constraints = (Constraints) request.getAttribute("constraints");
    LocalDate jour = (LocalDate) request.getAttribute("jour");

    LocalTime currentSlot = constraints.start;
    List<String> creneaux = new ArrayList<>();

    while (currentSlot.isBefore(constraints.end)) {
        creneaux.add(currentSlot.toString() + " - " + currentSlot.plusMinutes(constraints.minutesBetweenSlots));
        currentSlot = currentSlot.plusMinutes(constraints.minutesBetweenSlots);
    }
%>
<html>
<head>
    <title>Créneaux pour un jour</title>
    <!-- Import des CSS et autres ressources -->
</head>
<body>
    <div class="container">
        <h1>Créneaux pour <%= jour %></h1>

        <div class="creneaux-list">
            <ul>
                <% for (String creneau : creneaux) { %>
                    <li>
                        <%= creneau %>
                        <button onclick="ajouterReservation('<%= creneau %>')">Réserver</button>
                    </li>
                <% } %>
            </ul>
        </div>
    </div>

    <script>
        function ajouterReservation(creneau) {
            console.log("Réservation pour " + creneau);
            // Logique de réservation
        }
    </script>
</body>
</html>