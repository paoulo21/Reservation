import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/calendrier")
public class CalendrierServlet extends HttpServlet {
    // Paramètres de connexion à la base de données PostgreSQL
    
    private CalendrierDAO dao = new CalendrierDAO();

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
        clickCounters = dao.chargerClickCounters(dateCourante);

        // Gestion des compteurs de clics par jour du mois
        request.setAttribute("clickCounters", clickCounters);
        System.out.println("classes");
        // Génération du fichier JSP
        request.getRequestDispatcher("/cal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Incrémentation du compteur de clics sur une case spécifique
        String jourClique = request.getParameter("jour");
        if (jourClique != null) {
            LocalDate dateClique = LocalDate.parse(jourClique);

            // Incrémenter le compteur dans la base de données
            dao.incrementerClickCounter(dateClique);
        }
        // Redirection vers la page du calendrier
        response.sendRedirect("calendrier?mois=" + request.getParameter("mois"));
    }

    
}
