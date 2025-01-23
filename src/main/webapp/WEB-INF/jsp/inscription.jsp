<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h1>Inscription</h1>
        <form action="/inscription" method="POST" enctype="multipart/form-data">
            <div class="mb-3">
                <label for="email" class="form-label">EMail</label>
                <input type="text" class="form-control" id="email" name="email" required>
            </div>
            <div class="mb-3">
                <label for="nom" class="form-label">Nom</label>
                <input type="text" class="form-control" id="nom" name="nom" required>
            </div>
            <div class="mb-3">
                <label for="prenom" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="prenom" name="prenom" required>
            </div>
            <div class="mb-3">
                <label for="mdp" class="form-label">Mot de passe</label>
                <input type="password" class="form-control" id="mdp" name="mdp" required>
            </div>
            <div class="mb-3">
                <label for="image" class="form-label">Image de Profil</label>
                <input type="file" id="image" name="image" class="form-control" accept="image/*">
            </div>
            <button type="submit" class="btn btn-primary">S'inscrire</button>
            <a href="/connexion" class="btn btn-secondary">Déjà un compte ?</a>
            <div class="alert alert-danger mt-3" style="display: ${not empty errorMessage ? 'block' : 'none'};">
                ${errorMessage}
            </div>
        </form>
    </div>
</body>
</html>
