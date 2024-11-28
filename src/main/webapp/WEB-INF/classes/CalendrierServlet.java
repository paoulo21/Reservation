import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Creneaux.Constraints;
import DAO.CalendrierDAO;

@WebServlet("/calendrier")
public class CalendrierServlet extends HttpServlet {
    
    private CalendrierDAO dao = new CalendrierDAO();
    private Map<String, Integer> reservationCounters = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String moisParam = request.getParameter("mois");
        LocalDate dateCourante = (moisParam != null) ? LocalDate.parse(moisParam) : LocalDate.now();
        
        int premierJourDuMois = dateCourante.withDayOfMonth(1).getDayOfWeek().getValue();
        int nombreDeJours = dateCourante.lengthOfMonth();
        
        request.setAttribute("dateCourante", dateCourante);
        request.setAttribute("premierJourDuMois", premierJourDuMois);
        request.setAttribute("nombreDeJours", nombreDeJours);
        
        //clickCounters = dao.chargerClickCounters(dateCourante);
        reservationCounters = dao.chargerCreneaux(dateCourante); // Charger les compteurs de réservations
        Constraints constraints = dao.genererCreneaux(2);

        System.out.println(test());

        request.setAttribute("constraints", constraints);
        //request.setAttribute("clickCounters", clickCounters);
        request.setAttribute("reservationCounters", reservationCounters); // Passer les compteurs de réservations à la JSP
        
        request.getRequestDispatcher("/cal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jourClique = request.getParameter("jour");
        if (jourClique != null) {
            LocalDate dateClique = LocalDate.parse(jourClique);
            dao.incrementerClickCounter(dateClique);
        }
        response.sendRedirect("calendrier?mois=" + request.getParameter("mois"));
    }

    public String test(){
        StringBuilder sb = new StringBuilder("ReservationCounters: {");

        reservationCounters.forEach((key, value) -> {
            sb.append(key).append("=").append(value).append(", ");
        });

        if (!reservationCounters.isEmpty()) {
            sb.setLength(sb.length() - 2); // Supprime la dernière virgule et l'espace
        }

        sb.append("}");
        return sb.toString();
    }
}