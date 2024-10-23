import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendrierDAO {
    DS ds;
    Connection con;
    private static final String JDBC_URL = "jdbc:postgresql://psqlserv/but3?allowMultiQueries=true";
    private static final String JDBC_USER = "paullouisgomisetu";
    private static final String JDBC_PASSWORD = "moi";

    public CalendrierDAO() {
        ds = new DS();
        con = ds.getConnection();
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
    
}