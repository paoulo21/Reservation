package saeWeb;

import POJO.UtilisateurRepository;
import POJO.Reservation;
import POJO.ReservationRepository;
import POJO.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // Afficher les infos personnelles
    @GetMapping("/infos")
    public String afficherInfos(HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("principal");
        if (utilisateur == null) {
            return "redirect:/connexion";
        }
        model.addAttribute("utilisateur", utilisateur);
        return "infos";
    }

    // Modifier les infos personnelles
    @PostMapping("/modifierInfos")
    public String modifierInfos(
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("mdp") String mdp,
            HttpSession session,
            Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("principal");
        if (utilisateur == null) {
            return "redirect:/connexion"; // Redirige si non connecté
        }

        // Mise à jour des informations
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        if (mdp != null && !mdp.isEmpty()) {
            utilisateur.setMdp(md5(mdp));
        }

        utilisateurRepository.save(utilisateur); // Sauvegarde dans la base
        session.setAttribute("principal", utilisateur); // Mise à jour en session
        model.addAttribute("successMessage", "Informations mises à jour avec succès !");
        return "infos";
    }

    @GetMapping("/mesReservations")
    public String afficherReservations(HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("principal");
        if (utilisateur == null) {
            return "redirect:/connexion"; // Redirige si non connecté
        }

        model.addAttribute("reservations", reservationRepository.findByUtilisateur(utilisateur));
        return "mesReservations";
    }

    @PostMapping("/annulerReservations")
    public String annulerReservations(@RequestParam("reservationId") Long reservationId, HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("principal");
        if (utilisateur == null) {
            return "redirect:/connexion"; // Redirige si non connecté
        }

        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation != null && reservation.getUtilisateur().getId().equals(utilisateur.getId())) {
            reservationRepository.delete(reservation);
            model.addAttribute("successMessage", "La Reservation a été annulé avec succès.");
        } else {
            model.addAttribute("errorMessage", "Impossible d'annuler cette Reservation.");
        }

        return "redirect:/mesReservations";
    }

    private String md5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

