import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/calendrier")
public class CalendrierServlet extends HttpServlet {
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
            clickCounters.put(jourClique, clickCounters.getOrDefault(jourClique, 0) + 1);
        }
        // Redirection vers la page du calendrier
        response.sendRedirect("calendrier?mois=" + request.getParameter("mois"));
    }
}
