package saeWeb;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import POJO.Utilisateur;
import POJO.UtilisateurRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Controller
public class ConnexionController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JavaMailSender sender;


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
                return "redirect:/calendrier"; // Redirection en cas d'erreur
            }
        }

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("paullouis.gomis.etu@univ-lille.fr");
        helper.setTo(utilisateur.getEmail());
        helper.setSubject("Nouveau compte sur le site de réservation");
        helper.setText("Votre compte sur le site de réservation a été confirmé !" );
        sender.send(message);

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
