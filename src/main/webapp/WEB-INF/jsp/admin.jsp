<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil Administrateur</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Bienvenue Administrateur</h1>

        <div class="list-group">
            <!-- Lien vers la page Toutes Informations -->
            <a href="/admin/toutesinfos" class="list-group-item list-group-item-action">
                Gérer les informations des utilisateurs
            </a>

            <!-- Lien vers la page Toutes Réservations -->
            <a href="/admin/toutesreservations" class="list-group-item list-group-item-action">
                Gérer les réservations
            </a>
        </div>
    </div>
</body>
</html>
