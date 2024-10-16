import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/calendrier")
public class CalendrierServlet extends HttpServlet {
    // Paramètres de connexion à la base de données PostgreSQL
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/votre_base";
    private static final String JDBC_USER = "votre_utilisateur";
    private static final String JDBC_PASSWORD = "votre_mot_de_passe";

    // Dictionnaire pour stocker les compteurs de clics par date
    private Map<String, Integer> clickCounters = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération de la date courante ou de la date passée en paramètre
        String moisParam = request.getParameter("mois");
        LocalDate dateCourante = (moisParam != null) ? LocalDate.parse(moisParam) : LocalDate.now();

        // Création des informations pour le calendrier
        int premierJourDuMois = dateCourante.withDayOfMonth(1).getDayOfWeek().getValue(); // Lundi = 1
        int nombreDeJours = dateCourante.lengthOfMonth();

        request.setAttribute("dateCourante", dateCourante);
        request.setAttribute("premierJourDuMois", premierJourDuMois);
        request.setAttribute("nombreDeJours", nombreDeJours);

        // Charger les compteurs depuis la base de données pour ce mois
        chargerClickCounters(dateCourante);

        // Gestion des compteurs de clics par jour du mois
        request.setAttribute("clickCounters", clickCounters);

        // Génération du fichier JSP
        request.getRequestDispatcher("/calendrier.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Incrémentation du compteur de clics sur une case spécifique
        String jourClique = request.getParameter("jour");
        if (jourClique != null) {
            LocalDate dateClique = LocalDate.parse(jourClique);

            // Incrémenter le compteur dans la base de données
            incrementerClickCounter(dateClique);
        }
        // Redirection vers la page du calendrier
        response.sendRedirect("calendrier?mois=" + request.getParameter("mois"));
    }

    // Charger les compteurs depuis la base de données pour un mois donné
    private void chargerClickCounters(LocalDate date) {
        clickCounters.clear();
        String sql = "SELECT date, clicks FROM click_counters WHERE date >= ? AND date <= ?";

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
    }

    // Incrémenter le compteur de clics pour un jour donné dans la base de données
    private void incrementerClickCounter(LocalDate date) {
        String selectSql = "SELECT clicks FROM click_counters WHERE date = ?";
        String updateSql = "UPDATE click_counters SET clicks = clicks + 1 WHERE date = ?";
        String insertSql = "INSERT INTO click_counters (date, clicks) VALUES (?, 1)";

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
