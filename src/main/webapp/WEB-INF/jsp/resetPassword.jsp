<form action="/resetPassword" method="post">
    <input type="hidden" name="token" value="${token}">
    <div>
        <label for="password">Nouveau mot de passe :</label>
        <input type="password" id="password" name="password" required>
    </div>
    <div>
        <label for="confirmPassword">Confirmez le mot de passe :</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required>
    </div>
    <button type="submit">Réinitialiser</button>
</form>
