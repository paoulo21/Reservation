package DAO;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import POJO.Constraints;

public class CalendrierDAO {
    DS ds;
    Connection con;
    private static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "moi";

    public CalendrierDAO() {
        //ds = new DS();
        //con = ds.getConnection();
    }

    // Charger les compteurs depuis la base de données pour un mois donné
    public Map<String, Integer> chargerClickCounters(LocalDate date) {
        Map<String, Integer> clickCounters = new HashMap<>();
        String sql = "SELECT date, clicks FROM click_counters WHERE date >= ? AND date <= ?";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDate premierJour = date.withDayOfMonth(1);
            LocalDate dernierJour = date.withDayOfMonth(date.lengthOfMonth());

            ps.setDate(1, Date.valueOf(premierJour));
            ps.setDate(2, Date.valueOf(dernierJour));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate jour = rs.getDate("date").toLocalDate();
                int clicks = rs.getInt("clicks");
                clickCounters.put(jour.toString(), clicks);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clickCounters;
    }

    // Incrémenter le compteur de clics pour un jour donné dans la base de données
    public void incrementerClickCounter(LocalDate date) {
        String selectSql = "SELECT clicks FROM click_counters WHERE date = ?";
        String updateSql = "UPDATE click_counters SET clicks = clicks + 1 WHERE date = ?";
        String insertSql = "INSERT INTO click_counters (date, clicks) VALUES (?, 1)";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Vérifier si le jour existe déjà dans la table
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setDate(1, Date.valueOf(date));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Si le jour existe, on incrémente
                    try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                        psUpdate.setDate(1, Date.valueOf(date));
                        psUpdate.executeUpdate();
                    }
                } else {
                    // Sinon, on insère un nouveau record
                    try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                        psInsert.setDate(1, Date.valueOf(date));
                        psInsert.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Constraints genererCreneaux(int contrainteId) {
    int[] enabledDays = new int[]{};
    int maxPerSlot = 0;
    int minutesBetweenSlots = 0;
    LocalTime start = null;
    LocalTime end = null;
    String name = "";
    try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    // Exécution de la requête SQL pour récupérer les détails de la contrainte
    try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
        // Ici vous exécutez la requête SQL pour récupérer les détails de la contrainte en utilisant l'ID fourni
        // Utilisez PreparedStatement pour éviter les injections SQL
        // Exemple simplifié :
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM Constraints WHERE id = ?");
        preparedStatement.setInt(1, contrainteId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        // Récupérer les données de la contrainte
        if (resultSet.next()) {
            Array enabledDaysArray = resultSet.getArray("enabled_days");
            Integer[] enabledDaysInteger = (Integer[]) enabledDaysArray.getArray();

            // Convert Integer array to int array
            enabledDays = Arrays.stream(enabledDaysInteger).mapToInt(Integer::intValue).toArray();
            maxPerSlot = resultSet.getInt("max_per_slot");
            minutesBetweenSlots = resultSet.getInt("minutes_between_slots");
            start = resultSet.getTime("start").toLocalTime();
            end = resultSet.getTime("end").toLocalTime();
            name = resultSet.getString("name");
        } else {
            // Gérer le cas où aucune contrainte n'est trouvée pour l'ID donné
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Gérer les erreurs de connexion ou d'exécution de requête
    }
    List<Integer> listEnab = Arrays.stream(enabledDays)
      .boxed()
      .collect(Collectors.toList());;
    // Générer les créneaux en fonction des détails de la contrainte
    Constraints creneaux = new Constraints(listEnab,maxPerSlot,minutesBetweenSlots,start,end,name);
    // Ajoutez la logique de génération de créneaux ici en utilisant les détails récupérés de la base de données
    
    return creneaux;
}

    // Charger les compteurs depuis la base de données pour un mois donné
    public Map<String, Integer> chargerCreneaux(LocalDate date) {
        Map<String, Integer> reservationCounters = new HashMap<>();
        String sql = "SELECT DATE(date_heure) AS jour, COUNT(*) AS total_reservations FROM Reservation WHERE date_heure >= ? AND date_heure <= ? GROUP BY DATE(date_heure)";
        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            LocalDate premierJour = date.withDayOfMonth(1);
            LocalDate dernierJour = date.withDayOfMonth(date.lengthOfMonth());
    
            ps.setDate(1, Date.valueOf(premierJour));
            ps.setDate(2, Date.valueOf(dernierJour));
    
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                LocalDate jour = rs.getDate("jour").toLocalDate();
                int totalReservations = rs.getInt("total_reservations");
                reservationCounters.put(jour.toString(), totalReservations);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservationCounters;
    }

    public Map<String, Integer> chargerCreneauxParJour(LocalDate date, int minutesEntreCreneaux) {
        Map<String, Integer> creneauCounters = new HashMap<>();
        String sql = "SELECT date_heure, COUNT(*) AS total_reservations " +
                     "FROM Reservation " +
                     "WHERE DATE(date_heure) = ? " +
                     "GROUP BY date_heure " +
                     "ORDER BY date_heure";
        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setDate(1, Date.valueOf(date));
            
            ResultSet rs = ps.executeQuery();
            
            // Itérer sur chaque réservation et les classer par créneau horaire
            while (rs.next()) {
                LocalDateTime dateHeure = rs.getTimestamp("date_heure").toLocalDateTime();
                int totalReservations = rs.getInt("total_reservations");
                
                // Calcul du créneau : ex. "09:00-09:30" pour des créneaux de 30 minutes
                LocalTime startTime = dateHeure.toLocalTime().withMinute((dateHeure.getMinute() / minutesEntreCreneaux) * minutesEntreCreneaux).withSecond(0).withNano(0);
                LocalTime endTime = startTime.plusMinutes(minutesEntreCreneaux);
                
                String creneau = startTime + "-" + endTime;
                
                // Ajouter ou incrémenter le créneau dans la map
                creneauCounters.put(creneau, creneauCounters.getOrDefault(creneau, 0) + totalReservations);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return creneauCounters;
    }

    public boolean ajouterReservation(LocalDate jour, String creneau, int utilisateurId) {
        // Convertir le créneau en début et fin
        String[] heures = creneau.split("-");
        if (heures.length != 2) {
            throw new IllegalArgumentException("Créneau invalide : " + creneau);
        }
        
        LocalTime debut = LocalTime.parse(heures[0]);
        
        LocalDateTime dateHeureDebut = LocalDateTime.of(jour, debut);
    
        String sql = "INSERT INTO Reservation (id_utilisateur, date_heure) VALUES (?, ?)";
    
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, utilisateurId); // ID de l'utilisateur
            ps.setTimestamp(2, Timestamp.valueOf(dateHeureDebut)); // Date et heure du créneau
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    
}