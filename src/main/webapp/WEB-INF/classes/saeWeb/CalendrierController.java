package saeWeb;

import POJO.Constraints;
import POJO.ConstraintsRepository;
import POJO.CreneauSuppr;
import POJO.CreneauSupprRepository;
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
import java.util.stream.Collectors;

@Controller
public class CalendrierController {

    @Autowired
    private ConstraintsRepository constraintsRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CreneauSupprRepository creneauSupprRepository;

    private Map<String, Integer> reservationCounters = new HashMap<>();

    @GetMapping("/")
    public String redirectCalendrier(Model model){
        return "redirect:/calendrier";
    }

    @GetMapping("/calendrier")
    public String afficherCalendrier(
            @RequestParam(name = "mois", required = false) String moisParam,
            Model model) {

        LocalDate dateCourante = (moisParam != null) ? LocalDate.parse(moisParam) : LocalDate.now();

        int premierJourDuMois = dateCourante.withDayOfMonth(1).getDayOfWeek().getValue();
        int nombreDeJours = dateCourante.lengthOfMonth();

        Constraints constraints = constraintsRepository.findById(2).orElse(null);

        reservationCounters = chargerCreneaux(dateCourante);

        Map<LocalDate, Long> deletedCounters = chargerCreneauxSuppr(dateCourante);
        
        model.addAttribute("dateCourante", dateCourante);
        model.addAttribute("premierJourDuMois", premierJourDuMois);
        model.addAttribute("nombreDeJours", nombreDeJours);
        model.addAttribute("constraints", constraints);
        model.addAttribute("reservationCounters", reservationCounters);
        model.addAttribute("deletedCounters", deletedCounters);

        return "cal";
    }
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
    private Map<LocalDate, Long> chargerCreneauxSuppr(LocalDate dateCourante) {
    LocalDate premierJour = dateCourante.withDayOfMonth(1);
    LocalDate dernierJour = dateCourante.withDayOfMonth(dateCourante.lengthOfMonth());

    List<CreneauSuppr> creneauxSuppr = creneauSupprRepository.findAll();

    return creneauxSuppr.stream()
            .filter(c -> !c.getDateHeure().toLocalDate().isBefore(premierJour) &&
                         !c.getDateHeure().toLocalDate().isAfter(dernierJour))
            .collect(Collectors.groupingBy(
                    c -> c.getDateHeure().toLocalDate(),
                    Collectors.counting()
            ));
}

}
