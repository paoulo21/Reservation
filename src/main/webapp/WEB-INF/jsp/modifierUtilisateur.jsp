<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="POJO.Utilisateur" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    Utilisateur utilisateur = (Utilisateur) request.getAttribute("utilisateur");
    if (utilisateur == null) {
        response.sendRedirect("toutesInfos");
        return;
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="title.modifierUtilisateur"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4"><spring:message code="title.modifierUtilisateur"/></h1>
    <form action="enregistrerModification" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="<%= utilisateur.getId() %>">
        <input type="hidden" name="mdp" value="<%= utilisateur.getMdp() %>">
        
        <div class="mb-3">
            <label for="nom" class="form-label"><spring:message code="label.nom"/></label>
            <input type="text" id="nom" name="nom" class="form-control" value="<%= utilisateur.getNom() %>" required>
        </div>
        
        <div class="mb-3">
            <label for="prenom" class="form-label"><spring:message code="label.prenom"/></label>
            <input type="text" id="prenom" name="prenom" class="form-control" value="<%= utilisateur.getPrenom() %>" required>
        </div>
        
        <div class="mb-3">
            <label for="email" class="form-label"><spring:message code="label.email"/></label>
            <input type="email" id="email" name="email" class="form-control" value="<%= utilisateur.getEmail() %>" required>
        </div>
        
        <div class="mb-3">
            <label for="role" class="form-label"><spring:message code="label.role"/></label>
            <select id="role" name="role" class="form-select" required>
                <option value="User" <%= utilisateur.getRole().equals("User") ? "selected" : "" %>><spring:message code="label.utilisateur"/></option>
                <option value="Admin" <%= utilisateur.getRole().equals("Admin") ? "selected" : "" %>><spring:message code="label.admin"/></option>
            </select>
        </div>

        <div class="mb-3">
            <label for="image" class="form-label"><spring:message code="label.image"/></label>
            <input type="file" id="image" name="image" class="form-control" accept="image/*">
        </div>

        <button type="submit" class="btn btn-primary"><spring:message code="button.enregistrer"/></button>
        <a href="toutesInfos" class="btn btn-secondary"><spring:message code="button.annuler"/></a>
    </form>
</div>
</body>
</html>
