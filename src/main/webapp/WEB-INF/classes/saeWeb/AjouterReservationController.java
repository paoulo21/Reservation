package saeWeb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import POJO.Reservation;
import POJO.ReservationRepository;
import POJO.Utilisateur;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class AjouterReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JavaMailSender sender;


    @PostMapping("/ajouterReservation")
    public String ajouterReservation(
            @RequestParam("jour") String jourStr,
            @RequestParam("creneau") String creneau,
            HttpSession session,
            Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("principal");
    
            if (utilisateur == null) {      
        
            model.addAttribute("errorMessage", "Cette action nécessite d'être connecté.");
            return "redirect:/connexion"; // Redirige vers la page de connexion
        }
        //Long utilisateurId = utilisateur.getsId();
        try {
        LocalDate jour = LocalDate.parse(jourStr);
        //Utilisateur utilisateur = utilisateurRepository.findById((long) utilisateurId)
        //    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec l'ID : " + utilisateurId));


        String heureDebutStr = creneau.split("-")[0];
        LocalTime heureDebut = LocalTime.parse(heureDebutStr);

        Reservation reservation = new Reservation();
        reservation.setDateHeure(LocalDateTime.of(jour, heureDebut));
        reservation.setUtilisateur(utilisateur);
        reservationRepository.save(reservation);

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("paullouis.gomis.etu@univ-lille.fr");
        helper.setTo(utilisateur.getEmail());
        helper.setSubject("Confirmation de réservation");
        helper.setText("Votre réservation pour le "+jourStr+" de " + creneau +" a été confirmé !" );
        sender.send(message);

         // Ajouter des attributs pour la vue de succès
        model.addAttribute("jour", jourStr);
        model.addAttribute("creneau", creneau);
            // Redirige vers la page de succès
        return "successPage"; // successPage.jsp

        } catch (Exception e) {
            // Gestion d'erreur, ajouter un message explicite
            model.addAttribute("errorMessage", "Impossible d'effectuer la réservation : " + e.getMessage());
            return "errorPage"; // errorPage.jsp
        }
    }
}
