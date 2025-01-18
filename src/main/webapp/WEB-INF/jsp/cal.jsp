<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="POJO.Constraints" %>
<%@ page import="POJO.Utilisateur" %>
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
        <% if (principal.getRole().equals("Admin")) { %>
            <a href="admin" class="btn btn-link">Panneau Administrateur</a>
        <% } %>
        <img src="/utilisateurs/${principal.id}/image" alt="Image de Profil" style="max-width: 200px; height: auto;"/>
    </p>
<% } %>
    <div class="container">
        
        <%
            java.time.LocalDate dateCourante = (java.time.LocalDate) request.getAttribute("dateCourante");
            int premierJourDuMois = (int) request.getAttribute("premierJourDuMois");
            int nombreDeJours = (int) request.getAttribute("nombreDeJours");
            Constraints constraints = (Constraints) request.getAttribute("constraints");
            java.util.Map<LocalDate, Long> deletedCounters = (java.util.Map<LocalDate, Long>) request.getAttribute("deletedCounters");
            java.util.Map<String, Integer> reservationCounters = 
                (java.util.Map<String, Integer>) request.getAttribute("reservationCounters");
        %>
        <h1>Calendrier <%= constraints.getName() %></h1>
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
                                / constraints.getMinutesBetweenSlots() * constraints.getMaxPerSlot())-(deletedCounters.getOrDefault(currentDate,new Long(0))*constraints.getMaxPerSlot()) : 0;

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
