<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="POJO.Constraints" %>
<%@ page import="POJO.CreneauSuppr" %>
<%@ page import="POJO.Utilisateur" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    Constraints constraints = (Constraints) request.getAttribute("constraints");
    LocalDate jour = (LocalDate) request.getAttribute("jour");
    Map<String, Integer> creneauxComplets = (Map<String, Integer>) request.getAttribute("creneauxComplets");
    List<CreneauSuppr> creneauxSuppr = (List<CreneauSuppr>) request.getAttribute("creneauxSuppr");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="title.creneaux" arguments="<%= jour %>"/></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script>
        function confirmDeletion(message) {
            return confirm(message);
        }
    </script>
</head>
<body class="bg-light">
    <% 
        Utilisateur principal = (Utilisateur)session.getAttribute("principal"); 
        %>
        <% if (principal == null) { %>
    <a href="connexion" class="btn btn-primary"><spring:message code="button.connexion"/></a>
    <a href="inscription" class="btn btn-secondary"><spring:message code="button.creerCompte"/></a>
<% } else { %>
    <p>
    <img src="/utilisateurs/${principal.id}/image" style="max-width: 200px; height: auto;"/>
        <spring:message code="label.bienvenue" arguments="<%= principal.getNom() %>"/>
        <a href="calendrier" class="btn btn-link"><spring:message code="button.calendrier"/></a>
        <a href="infos" class="btn btn-link"><spring:message code="label.mesInfos"/></a>
        <a href="mesReservations" class="btn btn-link"><spring:message code="label.mesReservations"/></a>
        <% if (principal.getRole().equals("Admin")) { %>
            <a href="admin" class="btn btn-link"><spring:message code="button.panneauAdmin"/></a>
        <% } %>
        <a href="deconnexion" class="btn btn-secondary"><spring:message code="button.deconnexion"/></a>
    </p>
<% } %>
    <div class="container my-5">
        <h1 class="text-center mb-4"><spring:message code="title.creneaux" arguments="<%= jour.toString() %>"/></h1>
        <table class="table table-striped table-bordered text-center">
            <thead class="table-dark">
                <tr>
                    <th><spring:message code="label.creneau"/></th>
                    <th><spring:message code="label.reservationsActuelles" arguments="<%= constraints.getMaxPerSlot() %>"/></th>
                    <th><spring:message code="label.actions"/></th>
                    <% if (principal != null && principal.getRole().equals("Admin")) { %>
                        <th><spring:message code="label.supprimer"/></th>
                    <% } %>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Map.Entry<String, Integer> entry : creneauxComplets.entrySet()) {
                        String creneau = entry.getKey();
                        String[] creneauTab = creneau.split("-");
                        int reservations = entry.getValue();
                        
                        if (creneauxSuppr.stream().noneMatch(creneauSuppr -> 
                                creneauSuppr.getDateHeure().toLocalTime().equals(LocalTime.parse(creneauTab[0] + ":00")))) {
                %>
                <tr>
                    <td><%= creneau %></td>
                    <td><%= reservations %> / <%= constraints.getMaxPerSlot() %></td>
                    <td>
                        <% if (reservations < constraints.getMaxPerSlot()) { %>
                            <form method="post" action="ajouterReservation">
                                <input type="hidden" name="jour" value="<%= jour.toString() %>">
                                <input type="hidden" name="creneau" value="<%= creneau %>">
                                <button type="submit" class="btn btn-primary"><spring:message code="button.reserver"/></button>
                            </form>
                        <% } else { %>
                            <span class="badge bg-danger"><spring:message code="label.complet"/></span>
                        <% } %>
                    </td>
                    <% if (principal != null && principal.getRole().equals("Admin")) { %>
                        <td>
                            <form method="post" action="supprimerCreneau" onsubmit="return confirmDeletion('<spring:message code="message.confirmationSuppressionCreneau"/>');">
                                <input type="hidden" name="dateHeure" value="<%= jour.toString() + "T" + creneauTab[0] + ":00" %>">
                                <button type="submit" class="btn btn-danger"><spring:message code="button.supprimer"/></button>
                            </form>
                        </td>
                    <% } %>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
        <div class="text-center mt-4">
            <a href="calendrier" class="btn btn-secondary"><spring:message code="button.retourCalendrier"/></a>
        </div>
        <% if (principal != null && principal.getRole().equals("Admin")) { %>
            <div class="text-center mt-4">
                <form method="post" action="supprimerReservationsJour" onsubmit="return confirmDeletion('<spring:message code="message.confirmationSuppressionToutesReservations"/>');">
                    <input type="hidden" name="jour" value="<%= jour.toString() %>">
                    <button type="submit" class="btn btn-danger"><spring:message code="button.supprimerToutesReservations"/></button>
                </form>
            </div>
        <% } %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
