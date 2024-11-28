<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Map" %>
<%@ page import="Creneaux.Constraints" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.util.Arrays" %>
<%
    // Récupération des attributs du servlet
    LocalDate dateCourante = (LocalDate) request.getAttribute("dateCourante");
    int premierJourDuMois = (Integer) request.getAttribute("premierJourDuMois");
    int nombreDeJours = (Integer) request.getAttribute("nombreDeJours");
    Map<String, Integer> reservationCounters = (Map<String, Integer>) request.getAttribute("reservationCounters");
%>
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

        <div class="navigation">
            <!-- Boutons pour changer de mois -->
            <form method="get" action="calendrier">
                <input type="hidden" name="mois" value="<%= dateCourante.minusMonths(1).toString() %>">
                <button type="submit" class="btn btn-primary">Précédent</button>
            </form>

            <form method="get" action="calendrier">
                <input type="hidden" name="mois" value="<%= dateCourante.plusMonths(1).toString() %>">
                <button type="submit" class="btn btn-primary">Suivant</button>
            </form>
        </div>

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
                    <% 
                    // Cases vides avant le premier jour du mois
                    for (int i = 1; i < premierJourDuMois; i++) { 
                    %>
                        <td></td>
                    <% 
                    } 
                    
                    // Affichage des jours du mois
                    for (int jour = 1; jour <= nombreDeJours; jour++) { 
                    %>
                        <td class="current-month" onclick="this.querySelector('form').submit();" style="cursor: pointer;">
                            <form method="get" action="creneaux" style="display: none;">
                                <input type="hidden" name="jour" value="<%= dateCourante.withDayOfMonth(jour).toString() %>">
                            </form>
                            <%
                            // Récupérer les contraintes depuis la requête
                            Constraints constraints = (Constraints) request.getAttribute("constraints");

                            // Récupérer le jour de la semaine actuel (1 pour dimanche, 2 pour lundi, ..., 7 pour samedi)
                            int currentDayOfWeek = LocalDate.of(dateCourante.getYear(),dateCourante.getMonthValue(),jour).getDayOfWeek().getValue();

                            int totalReservationsAvailable = 0;

                            // Vérifier si le jour actuel est activé dans les contraintes
                            if (Arrays.stream(constraints.getEnabledDays()).anyMatch(day -> day == currentDayOfWeek)) {
                                // Calculer le nombre de créneaux
                                LocalTime start = constraints.getStart();
                                LocalTime end = constraints.getEnd();
                                int minutesBetweenSlots = constraints.getMinutesBetweenSlots();
                                long duration = Duration.between(start, end).toMinutes();
                                int numberOfSlots = (int) (duration / minutesBetweenSlots);
                            
                                totalReservationsAvailable = numberOfSlots * constraints.getMaxPerSlot();
                            } else {
                                totalReservationsAvailable = 0; // Jour non activé
                            }                            
                            LocalDate dateComplete = LocalDate.of(dateCourante.getYear(),dateCourante.getMonthValue(),jour);
                            // Afficher le nombre total de réservations disponibles pour ce jour (0 si le jour n'est pas activé)
                            %>
                            <%= jour %>
                            <div>Réservations Actuelles : <%= reservationCounters.get(dateComplete.toString()) != null ? reservationCounters.get(dateComplete.toString()) : 0 %>/<%= totalReservationsAvailable %></div>
                        </td>
                        <% 
                        // Nouvelle ligne après chaque dimanche
                        if ((jour + premierJourDuMois - 1) % 7 == 0) { 
                        %>
                            </tr><tr>
                        <% 
                        } 
                    } 
                    %>
                </tr>
            </tbody>
        </table>
    </div>
</body>
</html>