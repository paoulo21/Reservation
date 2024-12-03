<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="POJO.Constraints" %>
<html>
<head>
    <title>Calendrier Mensuel</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        table {
            width: 100%;
        }
        td {
            width: 14%;
            height: 100px;
            border: 1px solid black;
            text-align: center;
            vertical-align: top;
        }
        .current-month {
            background-color: #f0f8ff;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Calendrier Mensuel</h1>
        <%
            java.time.LocalDate dateCourante = (java.time.LocalDate) request.getAttribute("dateCourante");
            int premierJourDuMois = (int) request.getAttribute("premierJourDuMois");
            int nombreDeJours = (int) request.getAttribute("nombreDeJours");
            Constraints constraints = (Constraints) request.getAttribute("constraints");
            java.util.Map<String, Integer> reservationCounters = 
                (java.util.Map<String, Integer>) request.getAttribute("reservationCounters");
        %>
        <!-- Boutons pour changer de mois -->
        <div class="navigation">
            <form method="get" action="/calendrier">
                <input type="hidden" name="mois" value="<%= dateCourante.minusMonths(1) %>">
                <button type="submit" class="btn btn-primary">Précédent</button>
            </form>

            <form method="get" action="/calendrier">
                <input type="hidden" name="mois" value="<%= dateCourante.plusMonths(1) %>">
                <button type="submit" class="btn btn-primary">Suivant</button>
            </form>
        </div>

        <!-- Affichage du calendrier -->
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Lundi</th>
                    <th>Mardi</th>
                    <th>Mercredi</th>
                    <th>Jeudi</th>
                    <th>Vendredi</th>
                    <th>Samedi</th>
                    <th>Dimanche</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <!-- Cases vides avant le premier jour du mois -->
                    <% for (int i = 1; i < premierJourDuMois; i++) { %>
                        <td></td>
                    <% } %>

                    <!-- Affichage des jours du mois -->
                    <% 
                        for (int jour = 1; jour <= nombreDeJours; jour++) { 
                            LocalDate currentDate = dateCourante.withDayOfMonth(jour);
                            boolean isEnabledDay = constraints != null &&
                                constraints.getEnabledDays().contains(currentDate.getDayOfWeek().getValue());

                            long maxReservations = isEnabledDay ? 
                                (Duration.between(constraints.getStart(), constraints.getEnd()).toMinutes() 
                                / constraints.getMinutesBetweenSlots() * constraints.getMaxPerSlot()) : 0;

                            int currentReservations = reservationCounters.getOrDefault(currentDate.toString(), 0);
                    %>
                        <td class="current-month" onclick="this.querySelector('form').submit();" style="cursor: pointer;">
                            <form method="get" action="/creneaux" style="display: none;">
                                <input type="hidden" name="jour" value="<%= currentDate %>">
                            </form>
                            <%= jour %>
                            <div>
                                Réservations Actuelles : <%= currentReservations %> / <%= maxReservations %>
                            </div>
                        </td>

                        <!-- Nouvelle ligne après chaque dimanche -->
                        <% if ((jour + premierJourDuMois - 1) % 7 == 0) { %>
                            </tr><tr>
                        <% } %>
                    <% } %>
                </tr>
            </tbody>
        </table>
    </div>
</body>
</html>
