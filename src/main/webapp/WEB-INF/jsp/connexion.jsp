<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="title.connexion"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
    <a href="calendrier" class="btn btn-link"><spring:message code="button.calendrier"/></a>
        <h1><spring:message code="title.connexion"/></h1>
        <form action="/connexion" method="POST">
            <div class="mb-3">
                <label for="mail" class="form-label"><spring:message code="label.email"/></label>
                <input type="text" class="form-control" id="mail" name="mail" required>
            </div>
            <div class="mb-3">
                <label for="mdp" class="form-label"><spring:message code="label.mdp"/></label>
                <input type="password" class="form-control" id="mdp" name="mdp" required>
            </div>
            <button type="submit" class="btn btn-primary"><spring:message code="button.connexion"/></button>
            <a href="/inscription" class="btn btn-secondary"><spring:message code="button.creerCompte"/></a>
            <a href="/forgotPassword" class="btn btn-secondary"><spring:message code="button.reinitialiser"/></a>
            <div class="alert alert-danger mt-3" style="display: ${not empty errorMessage ? 'block' : 'none'};">
                ${errorMessage}
            </div>
            <div class="alert alert-success mt-3" style="display: <%= request.getParameter("success") != null ? "block" : "none" %>;">
        <spring:message code="label.reinitialisationEnvoyee"/>
    </div>
        </form>
    </div>
</body>
</html>
