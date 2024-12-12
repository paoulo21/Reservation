package saeWeb;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // Enregistrer les modifications d'un utilisateur
    @PostMapping("/enregistrerModification")
    public String enregistrerModification(@ModelAttribute Utilisateur utilisateur, Model model) {
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

