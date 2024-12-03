package saeWeb;

import POJO.Constraints;
import POJO.ConstraintsRepository;
import POJO.Reservation;
import POJO.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CalendrierController {

    @Autowired
    private ConstraintsRepository constraintsRepository;

    @Autowired
    private ReservationRepository reservationRepository;  // Injecter le repository

    //@Autowired
    //private CalendrierDAO calendrierDAO; // Remplacer par un Repository si DAO est encore manuel

    private Map<String, Integer> reservationCounters = new HashMap<>();

    @GetMapping("/calendrier")
    public String afficherCalendrier(
            @RequestParam(name = "mois", required = false) String moisParam,
            Model model) {

        // Calculer la date courante en fonction du paramètre "mois"
        LocalDate dateCourante = (moisParam != null) ? LocalDate.parse(moisParam) : LocalDate.now();

        // Calcul des informations nécessaires pour l'affichage du calendrier
        int premierJourDuMois = dateCourante.withDayOfMonth(1).getDayOfWeek().getValue();
        int nombreDeJours = dateCourante.lengthOfMonth();

        // Récupérer les contraintes depuis la base
        Constraints constraints = constraintsRepository.findById(2).orElse(null);

        // Charger les créneaux de réservation
        reservationCounters = chargerCreneaux(dateCourante);

        // Ajouter les données au modèle
        model.addAttribute("dateCourante", dateCourante);
        model.addAttribute("premierJourDuMois", premierJourDuMois);
        model.addAttribute("nombreDeJours", nombreDeJours);
        model.addAttribute("constraints", constraints);
        model.addAttribute("reservationCounters", reservationCounters);

        // Retourner le nom de la vue JSP ou Thymeleaf
        return "cal"; // Assurez-vous que "cal.jsp" ou équivalent existe dans vos templates
    }

    /*@PostMapping("/calendrierr")
    public String enregistrerReservation(
            @RequestParam("jour") String jourClique,
            @RequestParam("mois") String mois) {

        if (jourClique != null) {
            LocalDate dateClique = LocalDate.parse(jourClique);
            calendrierDAO.incrementerClickCounter(dateClique);
        }

        // Redirection vers la même page après l'action POST
        return "redirect:/calendrier?mois=" + mois;
    }*/

    // Remplacer le DAO par le repository
    public Map<String, Integer> chargerCreneaux(LocalDate date) {
        Map<String, Integer> reservationCounters = new HashMap<>();
        
        LocalDateTime premierJour = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime dernierJour = date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59);

        // Utiliser le repository pour récupérer les réservations entre les deux dates
        List<Reservation> reservations = reservationRepository.findByDateHeureBetween(premierJour, dernierJour);

        // Organiser les résultats dans le format attendu (jour -> nombre de réservations)
        for (Reservation reservation : reservations) {
            LocalDate jour = reservation.getDateHeure().toLocalDate();  // Assurez-vous que vous récupérez la date de réservation
            reservationCounters.put(jour.toString(), reservationCounters.getOrDefault(jour.toString(), 0) + 1);
        }

        return reservationCounters;
    }
}
