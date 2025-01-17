package saeWeb;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import POJO.Reservation;
import POJO.ReservationRepository;
import POJO.Utilisateur;
import POJO.UtilisateurRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("")
    public String panneauAdmin(Model model) {
        return "admin";
    }

    @GetMapping("/toutesReservations")
    public String listerToutesReservations(Model model) {
        List<Reservation> reservations = reservationRepository.findAll();
        model.addAttribute("reservations", reservations);
        return "toutesReservations";
    }

    @PostMapping("/supprimerReservation")
    public String supprimerReservation(@RequestParam("reservationId") Long reservationId) {
        reservationRepository.deleteById(reservationId);
        return "redirect:/admin/reservations";
    }
    

    // Afficher la liste des utilisateurs
    @GetMapping("/toutesInfos")
    public String afficherListeUtilisateurs(Model model) {
        model.addAttribute("utilisateurs", utilisateurRepository.findAll());
        return "toutesInfos";
    }

    // Modifier un utilisateur (afficher le formulaire de modification)
    @PostMapping("/modifierUtilisateur")
    public String modifierUtilisateurForm(@RequestParam("id") Long id, Model model) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        if (utilisateur == null) {
            model.addAttribute("errorMessage", "Utilisateur non trouvé !");
            return "redirect:/admin/toutesInfos";
        }
        Utilisateur utilisateurConf = utilisateur.get();
        model.addAttribute("utilisateur", utilisateurConf);
        return "modifierUtilisateur"; // Page JSP pour le formulaire de modification
    }

    @PostMapping("/enregistrerModification")
    public String enregistrerModification(@RequestParam("id") Long id, 
                                        @RequestParam("mdp") String mdp,
                                        @RequestParam("image") MultipartFile imageFile, 
                                        @RequestParam("nom") String nom,
                                        @RequestParam("prenom") String prenom,
                                        @RequestParam("email") String email,
                                        @RequestParam("role") String role,
                                        Model model) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setRole(role);
        
        // Gérer l'image
        if (!imageFile.isEmpty()) {
            try {
                utilisateur.setImageProfil(imageFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Erreur lors de l'enregistrement de l'image.");
                return "redirect:/admin/toutesInfos"; // Redirection en cas d'erreur
            }
        }

        utilisateurRepository.save(utilisateur);
        model.addAttribute("successMessage", "Utilisateur modifié avec succès !");
        return "redirect:/admin/toutesInfos";
    }

    // Supprimer un utilisateur
    @PostMapping("/supprimerUtilisateur")
    public String supprimerUtilisateur(@RequestParam("id") Long id, Model model) {
        utilisateurRepository.deleteById(id);
        model.addAttribute("successMessage", "Utilisateur supprimé avec succès !");
        return "redirect:/admin/toutesInfos";
    }
}

