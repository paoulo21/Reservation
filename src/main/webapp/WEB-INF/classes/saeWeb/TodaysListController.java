package saeWeb;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import POJO.Reservation;
import POJO.ReservationRepository;

@RestController
@RequestMapping("/todayslist")
public class TodaysListController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/{date}")
    public ResponseEntity<?> getTodaysList(@PathVariable String date) {
        try {
            // Convertir la date en LocalDate
            LocalDate targetDate = LocalDate.parse(date);

            // Récupérer les réservations pour cette journée
            List<Reservation> reservations = reservationRepository.findByDateHeureBetween(
                    targetDate.atStartOfDay(),
                    targetDate.plusDays(1).atStartOfDay()
            );

            // Vérifier si des réservations existent
            if (reservations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune réservation trouvée pour cette date.");
            }

            return ResponseEntity.ok(reservations);

        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La date est au mauvais format. Utilisez le format AAAA-MM-JJ.");
        }
    }
}
