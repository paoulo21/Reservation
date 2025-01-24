<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="title.forgotPassword"/></title>
    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4><spring:message code="title.forgotPassword"/></h4>
                    </div>
                    <div class="card-body">

                        <!-- Formulaire de réinitialisation de mot de passe -->
                        <form method="post" action="/forgotPassword">
                            <div class="form-group">
                                <label for="email"><spring:message code="label.email"/></label>
                                <input type="email" id="email" name="email" class="form-control" required>
                            </div>

                            <button type="submit" class="btn btn-primary"><spring:message code="button.reinitialiser"/></button>
                        </form>

                        <!-- Affichage des messages de succès ou d'erreur -->
                        <%
                            String success = request.getParameter("success");
                            String error = request.getParameter("error");
                            if (success != null) {
                        %>
                            <div class="alert alert-success mt-3">
                                <spring:message code="label.reinitialisationEnvoyee"/>
                            </div>
                        <% 
                            } else if (error != null) {
                        %>
                            <div class="alert alert-danger mt-3">
                                <spring:message code="label.utilisateurNonTrouve"/>
                            </div>
                        <% 
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS et Popper.js -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>
