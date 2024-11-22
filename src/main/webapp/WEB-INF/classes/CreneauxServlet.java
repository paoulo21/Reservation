import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import Creneaux.*;

@WebServlet("/creneaux")
public class CreneauxServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération de l'ID de la contrainte à partir des paramètres de requête
        int contrainteId = 1/*Integer.parseInt(request.getParameter("contrainteId"))*/;

        // Récupération des données de la contrainte à partir de la base de données
        CalendrierDAO calendrierDAO = new CalendrierDAO();
        Constraints constraints = calendrierDAO.genererCreneaux(contrainteId);

        // Ici, vous devriez récupérer le jour pour lequel vous souhaitez afficher les créneaux
        String dateParam = request.getParameter("jour");

        LocalDate jour = LocalDate.now();

        if (dateParam != null && !dateParam.isEmpty()) {
            // Parser la date passée en argument
            jour = LocalDate.parse(dateParam);
        }
        //LocalDate jour = LocalDate.parse((String)request.getAttribute("jour")); // Exemple avec la date actuelle

        // Set des attributs pour la JSP
        request.setAttribute("constraints", constraints);
        request.setAttribute("jour", jour);
        System.out.println(constraints);
        // Dispatch vers la JSP creneaux.jsp
        request.getRequestDispatcher("/creneaux.jsp").forward(request, response);
    }
}