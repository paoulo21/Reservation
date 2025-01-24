<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="POJO.Utilisateur" %>
<%@ page import="POJO.Reservation" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
    <title><spring:message code="title.mesReservations"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<a href="infos" class="btn btn-link"><spring:message code="link.mesInfos"/></a>
<a href="calendrier" class="btn btn-link"><spring:message code="button.calendrier"/></a>
<a href="deconnexion" class="btn btn-secondary"><spring:message code="button.deconnexion"/></a>
<div class="container mt-5">
    <h1 class="mb-4"><spring:message code="title.mesReservations"/></h1>

    <%-- Message de succès si présent --%>
    <% String successMessage = (String) request.getAttribute("successMessage"); %>
    <% if (successMessage != null) { %>
        <div class="alert alert-success">
            <%= successMessage %>
        </div>
    <% } %>

    <%-- Message d'erreur si présent --%>
    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMessage != null) { %>
        <div class="alert alert-danger">
            <%= errorMessage %>
        </div>
    <% } %>

    <table class="table table-striped">
        <thead>
        <tr>
            <th><spring:message code="label.date"/></th>
            <th><spring:message code="label.creneau"/></th>
            <th><spring:message code="label.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
            if (reservations != null && !reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
        %>
            <tr>
                <td><%= reservation.getDateHeure().toLocalDate() %></td>
                <td><%= reservation.getDateHeure().toLocalTime() %></td>
                <td>
                    <form action="annulerReservations" method="post" style="display:inline;">
                        <input type="hidden" name="reservationId" value="<%= reservation.getId() %>">
                        <button type="submit" class="btn btn-danger btn-sm"><spring:message code="button.annuler"/></button>
                    </form>
                </td>
            </tr>
        <% 
                }
            } else { 
        %>
            <tr>
                <td colspan="3" class="text-center"><spring:message code="label.aucuneReservation"/></td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
