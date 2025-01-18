package POJO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreneauSupprRepository extends JpaRepository<CreneauSuppr, Long> {

    @Query("SELECT c FROM CreneauSuppr c WHERE CAST(c.dateHeure AS date) = :date")
List<CreneauSuppr> findByDay(@Param("date") LocalDate date);

}

