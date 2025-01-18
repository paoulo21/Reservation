package POJO;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "creneaux_suppr")
public class CreneauSuppr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    public CreneauSuppr() {
    }

    public CreneauSuppr(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    @Override
    public String toString() {
        return "CreneauSuppr{" +
                "id=" + id +
                ", dateHeure=" + dateHeure +
                '}';
    }
}

