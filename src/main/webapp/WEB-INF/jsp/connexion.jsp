<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
    <a href="calendrier" class="btn btn-link">Calendrier</a>
        <h1>Connexion</h1>
        <form action="/connexion" method="POST">
            <div class="mb-3">
                <label for="mail" class="form-label">Mail</label>
                <input type="text" class="form-control" id="mail" name="mail" required>
            </div>
            <div class="mb-3">
                <label for="mdp" class="form-label">Mot de passe</label>
                <input type="password" class="form-control" id="mdp" name="mdp" required>
            </div>
            <button type="submit" class="btn btn-primary">Se connecter</button>
            <a href="/inscription" class="btn btn-secondary">Pas de compte ?</a>
            <a href="/forgotPassword" class="btn btn-secondary">Mot de passe oublié</a>
            <div class="alert alert-danger mt-3" style="display: ${not empty errorMessage ? 'block' : 'none'};">
                ${errorMessage}
            </div>
            <div class="alert alert-success mt-3" style="display: <%= request.getParameter("success") != null ? "block" : "none" %>;">
        Votre mot de passe a été réinitialisé. Vous pouvez vous connecter.
    </div>
        </form>
    </div>
</body>
</html>
