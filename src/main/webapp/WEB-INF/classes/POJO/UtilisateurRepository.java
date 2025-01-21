package POJO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByemailAndMdp(String email, String mdp);
    Optional<Utilisateur> findById(Long id);
    List<Utilisateur> findByNom(String nom);
    boolean existsByEmail(String email);
    Optional<Utilisateur> findByEmail(String email);
}