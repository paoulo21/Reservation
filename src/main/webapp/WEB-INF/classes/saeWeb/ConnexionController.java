package saeWeb;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import POJO.PasswordResetToken;
import POJO.PasswordResetTokenRepository;
import POJO.Utilisateur;
import POJO.UtilisateurRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import service.EmailService;

@Controller
public class ConnexionController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;


    // Affichage du formulaire d'inscription
    @GetMapping("/inscription")
    public String afficherInscriptionForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "inscription";
    }

    @PostMapping("/inscription")
    public String enregistrerUtilisateur(
                                        @RequestParam("mdp") String mdp,
                                        @RequestParam("image") MultipartFile imageFile, 
                                        @RequestParam("nom") String nom,
                                        @RequestParam("prenom") String prenom,
                                        @RequestParam("email") String email,
                                        Model model, HttpSession session) throws MessagingException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMdp(mdp);
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setRole("User");
        
        // Gérer l'image
        if (!imageFile.isEmpty()) {
            try {
                utilisateur.setImageProfil(imageFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Erreur lors de l'enregistrement de l'image.");
                return "redirect:/inscription"; // Redirection en cas d'erreur
            }
        }

        emailService.sendSimpleMessage(utilisateur.getEmail(), "Nouveau compte sur le site de réservation",
                "Votre compte sur le site de réservation a été confirmé !");

        utilisateurRepository.save(utilisateur);
        session.setAttribute("principal", utilisateur);
        return "redirect:/calendrier";
    }

    // Connexion de l'utilisateur
    @GetMapping("/connexion")
    public String afficherConnexionForm() {
        return "connexion";
    }

    // Traitement de la connexion
    @PostMapping("/connexion")
    public String connecterUtilisateur(@RequestParam("mail") String mail, @RequestParam("mdp") String mdp, HttpSession session, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findByemailAndMdp(mail, md5(mdp));
        if (utilisateur != null) {
            session.setAttribute("principal", utilisateur);
            return "redirect:/calendrier";
        } else {
            model.addAttribute("errorMessage", "Mail ou mot de passe incorrect.");
            return "connexion"; // Redirection en cas d'échec de connexion
        }
    }

    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        session.invalidate(); // Invalide la session
        return "redirect:/connexion"; // Redirige vers la page de connexion
    }

    @GetMapping("/forgotPassword")
    public String mdpOublié(HttpSession session) {
        session.invalidate(); // Invalide la session
        return "forgotPassword"; // Redirige vers la page de connexion
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

        // Vérifiez si l'utilisateur existe
        if (utilisateurOpt.isEmpty()) {
            return "redirect:/forgotPassword?error=UtilisateurNonTrouvé";
        }

        // Supprimez d'éventuels anciens tokens
        passwordResetTokenRepository.deleteByEmail(email);

        // Générer un jeton unique
        String token = UUID.randomUUID().toString();

        // Créez un nouveau token
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpirationTime(LocalDateTime.now().plusHours(1));

        passwordResetTokenRepository.save(resetToken);

        // Envoyer un e-mail avec le lien de réinitialisation
        String resetLink = "http://localhost:8080/resetPassword?token=" + token;
        emailService.sendSimpleMessage(email, "Réinitialisation du mot de passe",
                "Cliquez sur ce lien pour réinitialiser votre mot de passe : " + resetLink + "\n Ce lien expire dans 1 heure.");

        return "redirect:/forgotPassword?success";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

        // Vérifier si le jeton existe dans la base de données
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(token);
        if (tokenOpt.isEmpty() || tokenOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            // Si le jeton est invalide ou expiré, rediriger vers une page d'erreur
            return "redirect:/resetPassword?error=InvalidOrExpiredToken";
        }

        // Ajouter le jeton au modèle pour l'utiliser dans le formulaire
        model.addAttribute("token", token);

        // Retourner la vue de réinitialisation du mot de passe
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword) {

        // Valider le token
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(token);
        if (tokenOpt.isEmpty() || tokenOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            return "redirect:/resetPassword?error=InvalidOrExpiredToken";
        }

        // Valider les mots de passe
        if (!password.equals(confirmPassword)) {
            return "redirect:/resetPassword?error=PasswordsDoNotMatch";
        }

        // Mettre à jour le mot de passe
        PasswordResetToken resetToken = tokenOpt.get();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        utilisateur.setMdp(md5(password));
        utilisateurRepository.save(utilisateur);

        // Supprimer le jeton après usage
        passwordResetTokenRepository.delete(resetToken);

        return "redirect:/login?success=PasswordReset";
    }

    // Fonction pour encoder un mot de passe avec MD5
    private String md5(String mdp) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(mdp.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
