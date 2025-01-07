package POJO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Méthode pour récupérer les réservations par mois
    List<Reservation> findByDateHeureBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT r.dateHeure, COUNT(r) FROM Reservation r GROUP BY r.dateHeure")
    List<Object[]> findUniqueDateHeuresAndCount();

    List<Reservation> findByUtilisateur(Utilisateur utilisateur);


    // Récupérer les réservations futures d'un utilisateur
    List<Reservation> findByUtilisateurAndDateHeureAfter(Utilisateur utilisateur, LocalDateTime dateHeure);

}
