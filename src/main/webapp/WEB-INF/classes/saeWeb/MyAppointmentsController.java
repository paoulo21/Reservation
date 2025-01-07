package saeWeb;

import java.time.LocalDateTime;
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
import POJO.Utilisateur;
import POJO.UtilisateurRepository;

@RestController
@RequestMapping("/myappointments")
public class MyAppointmentsController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/{name}")
    public ResponseEntity<?> getMyAppointments(@PathVariable String name) {
        try {
            // Rechercher les utilisateurs par nom
            List<Utilisateur> utilisateurs = utilisateurRepository.findByNom(name);

            if (utilisateurs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun utilisateur trouvé avec ce nom.");
            }

            if (utilisateurs.size() > 1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Plusieurs utilisateurs ont le même nom.");
            }

            // Récupérer les réservations futures de cet utilisateur
            Utilisateur utilisateur = utilisateurs.get(0);
            List<Reservation> reservations = reservationRepository.findByUtilisateurAndDateHeureAfter(
                    utilisateur, LocalDateTime.now()
            );

            return ResponseEntity.ok(reservations);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue.");
        }
    }
}

