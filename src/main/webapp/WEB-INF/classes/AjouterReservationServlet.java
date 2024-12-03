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

import DAO.CalendrierDAO;
import POJO.Constraints;

@WebServlet("/ajouterReservationServlet")
public class AjouterReservationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jourStr = request.getParameter("jour");
        String creneau = request.getParameter("creneau");
        int utilisateurId = 1; //(int) request.getSession().getAttribute("utilisateurId"); // ID utilisateur depuis la session

        LocalDate jour = LocalDate.parse(jourStr);

        CalendrierDAO calendrierDAO = new CalendrierDAO();
        boolean success = calendrierDAO.ajouterReservation(jour, creneau, utilisateurId);

        if (success) {
            // Réservation réussie, redirection ou message de succès
            request.setAttribute("jour", jourStr);
            request.setAttribute("creneau", creneau);
            response.sendRedirect("successPage.jsp");
        } else {
            // Réservation échouée, renvoyer un message d'erreur
            request.setAttribute("errorMessage", "Impossible d'effectuer la réservation.");
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }
}
