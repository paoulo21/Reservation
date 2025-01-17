package saeWeb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import POJO.Constraints;
import POJO.ConstraintsRepository;
import POJO.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CreneauxController {

    @Autowired
    private ConstraintsRepository constraintsRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/creneaux")
    public String afficherCreneaux(
            @RequestParam(name = "contrainteId", defaultValue = "2") int contrainteId,
            @RequestParam(name = "jour", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate jour,
            Model model) {

        // Récupération des contraintes depuis le repository
        Constraints constraints = constraintsRepository.findById(contrainteId)
                .orElseThrow(() -> new IllegalArgumentException("Contrainte introuvable"));

        // Si aucun jour n'est spécifié, prendre la date actuelle
        if (jour == null) {
            jour = LocalDate.now();
        }

        List<Object[]> reservationsData = reservationRepository.findUniqueDateHeuresAndCount();
        Map<String, Integer> reservationsParCreneau = new LinkedHashMap<>(); // Remplacez par votre logique

        for (Object[] result : reservationsData) {
            LocalDateTime dateTime = (LocalDateTime) result[0];  // La date et l'heure unique
            Long count = (Long) result[1];  // Le nombre de réservations pour cette dateHeure

            // Vérifiez si la date correspond au jour spécifié
            if (dateTime.toLocalDate().isEqual(jour)) {
                String temps = dateTime.toLocalTime().toString(); // Gardez seulement l'heure
                reservationsParCreneau.put(temps, count.intValue());
            }
        }
        System.out.println("///////////");
        // Générer les créneaux horaires complets
        Map<String, Integer> creneauxComplets = new LinkedHashMap<>();
        LocalTime start = constraints.getStart();
        LocalTime end = constraints.getEnd();
        int minutesEntreCreneaux = constraints.getMinutesBetweenSlots();

        while (!start.isAfter(end)) {
            LocalTime nextSlot = start.plusMinutes(minutesEntreCreneaux);
            String creneau = start + "-" + nextSlot;
            creneauxComplets.put(creneau, reservationsParCreneau.getOrDefault(start.toString(), 0));
            start = nextSlot;
        }

        // Ajouter les données au modèle
        model.addAttribute("constraints", constraints);
        model.addAttribute("jour", jour);
        model.addAttribute("creneauxComplets", creneauxComplets);

        return "creneaux"; // Retourne la vue `creneaux.jsp`
    }
}
