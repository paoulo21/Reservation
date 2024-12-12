package saeWeb.filtre;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"/admin/*", "/infos", "/mesReservations"})
public class ConnexionFiltre extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Object user = request.getSession().getAttribute("principal");
        if (user == null && !request.getRequestURI().contains("/login")) {
            response.sendRedirect("/connexion");
            return;
        }
        chain.doFilter(request, response);
    }
}
