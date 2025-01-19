<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur de Réservation</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container my-5">
        <h1 class="text-danger text-center">Erreur de Réservation</h1>
        <p class="text-center">Désolé, une erreur est survenue.</p>

        <div class="alert alert-danger text-center">
            <p><strong>Message d'erreur :</strong> <%= request.getAttribute("errorMessage") %></p>
        </div>

        <div class="text-center mt-4">
            <a href="calendrier" class="btn btn-secondary">Retour au calendrier</a>
        </div>
    </div>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
