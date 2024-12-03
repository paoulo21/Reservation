import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import DAO.CalendrierDAO;
import POJO.Constraints;
import POJO.ConstraintsRepository;

@WebServlet("/calendrier")
public class CalendrierServlet extends HttpServlet {
    
    private CalendrierDAO dao = new CalendrierDAO();
    private Map<String, Integer> reservationCounters = new HashMap<>();
    
    @Autowired
    private ConstraintsRepository constraintsRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String moisParam = request.getParameter("mois");
        LocalDate dateCourante = (moisParam != null) ? LocalDate.parse(moisParam) : LocalDate.now();
        
        int premierJourDuMois = dateCourante.withDayOfMonth(1).getDayOfWeek().getValue();
        int nombreDeJours = dateCourante.lengthOfMonth();
        
        request.setAttribute("dateCourante", dateCourante);
        request.setAttribute("premierJourDuMois", premierJourDuMois);
        request.setAttribute("nombreDeJours", nombreDeJours);

        reservationCounters = dao.chargerCreneaux(dateCourante);
        //Constraints constraints = dao.genererCreneaux(2);
        Constraints constraints = constraintsRepository.findById(2).orElse(null);

        request.setAttribute("constraints", constraints);
        request.setAttribute("reservationCounters", reservationCounters);
        
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
}