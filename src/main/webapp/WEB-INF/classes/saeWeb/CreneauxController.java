package saeWeb;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import POJO.Constraints;
import POJO.ConstraintsRepository;
import POJO.CreneauSuppr;
import POJO.CreneauSupprRepository;
import POJO.Reservation;
import POJO.ReservationRepository;
import POJO.Utilisateur;
import jakarta.mail.MessagingException;

@Controller
public class CreneauxController {

    @Autowired
    private ConstraintsRepository constraintsRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CreneauSupprRepository creneauSupprRepository;

    @Autowired
    private EmailService emailService;

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
        if (!constraints.getEnabledDays().contains(jour.getDayOfWeek().getValue())) {
            model.addAttribute("errorMessage", "Ce jour n'est pas disponible à la réservation ");
            return "errorPage";
        }
        List<Object[]> reservationsData = reservationRepository.findUniqueDateHeuresAndCount();
        Map<String, Integer> reservationsParCreneau = new LinkedHashMap<>();

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

        List<CreneauSuppr> creneauxSuppr = creneauSupprRepository.findByDay(jour);

        // Ajouter les données au modèle
        model.addAttribute("constraints", constraints);
        model.addAttribute("jour", jour);
        model.addAttribute("creneauxComplets", creneauxComplets);
        model.addAttribute("creneauxSuppr", creneauxSuppr);

        return "creneaux"; // Retourne la vue `creneaux.jsp`
    }
    @PostMapping("/supprimerCreneau")
    public String supprimerCreneau(@RequestParam("dateHeure") String dateHeureStr, Model model) throws MessagingException {
        // Convertir la date/heure en LocalDateTime
        LocalDateTime dateHeure = LocalDateTime.parse(dateHeureStr);

        // Enregistrer le créneau supprimé dans la base de données
        CreneauSuppr creneauSuppr = new CreneauSuppr();
        creneauSuppr.setDateHeure(dateHeure);
        creneauSupprRepository.save(creneauSuppr);

        // Supprimer les réservations liées au créneau supprimé
        List<Reservation> reservationsToDelete = reservationRepository.findByDateHeure(dateHeure);
        List<Utilisateur> utilisateursConcernes = reservationsToDelete.stream()
                .map(Reservation::getUtilisateur)
                .distinct()
                .collect(Collectors.toList());

        reservationRepository.deleteAll(reservationsToDelete);

        for (Utilisateur utilisateur : utilisateursConcernes) {
            emailService.sendSimpleMessage(utilisateur.getEmail(), "Annulation d'un créneau concernant une de vos réservation",
                "Votre réservation pour le "+ dateHeureStr +" a malheuresement dù être annulé suite à un empèchement");
        }

        return "redirect:/calendrier";
    }

    @PostMapping("/supprimerReservationsJour")
    public String supprimerReservationsJour(@RequestParam("jour") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate jour, Model model) throws MessagingException {
        // Récupérer les contraintes depuis le repository avec l'ID 2
        Constraints constraints = constraintsRepository.findById(2)
                .orElseThrow(() -> new IllegalArgumentException("Contrainte introuvable"));

        // Récupérer toutes les réservations pour le jour spécifié
        List<Reservation> reservationsToDelete = reservationRepository.findByDateHeureBetween(jour.atStartOfDay(), jour.plusDays(1).atStartOfDay());
        List<Utilisateur> utilisateursConcernes = reservationsToDelete.stream()
                .map(Reservation::getUtilisateur)
                .distinct()
                .collect(Collectors.toList());

        // Ajouter tous les créneaux du jour à la table CreneauSuppr
        LocalTime start = constraints.getStart();
        LocalTime end = constraints.getEnd();
        int minutesEntreCreneaux = constraints.getMinutesBetweenSlots();

        while (!start.isAfter(end)) {
            LocalDateTime dateTime = LocalDateTime.of(jour, start);
            CreneauSuppr creneauSuppr = new CreneauSuppr();
            creneauSuppr.setDateHeure(dateTime);
            creneauSupprRepository.save(creneauSuppr);
            start = start.plusMinutes(minutesEntreCreneaux);
        }

        // Supprimer les réservations
        reservationRepository.deleteAll(reservationsToDelete);

        // Envoyer un email aux utilisateurs concernés
        for (Utilisateur utilisateur : utilisateursConcernes) {
            emailService.sendSimpleMessage(utilisateur.getEmail(), "Annulation de toutes les réservations pour le " + jour,
                "Toutes vos réservations pour le " + jour + " ont été annulées.");
        }

        return "redirect:/calendrier";
    }
}
