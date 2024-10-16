<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Calendrier Mensuel</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        table {
            width: 100%;
        }
        td {
            width: 14%;
            height: 100px;
            border: 1px solid black;
            text-align: center;
            vertical-align: top;
        }
        .current-month {
            background-color: #f0f8ff;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Calendrier Mensuel</h1>

        <div class="navigation">
            <!-- Boutons pour changer de mois -->
            <form method="get" action="calendrier">
                <input type="hidden" name="mois" value="${dateCourante.minusMonths(1)}">
                <button type="submit" class="btn btn-primary">Précédent</button>
            </form>

            <form method="get" action="calendrier">
                <input type="hidden" name="mois" value="${dateCourante.plusMonths(1)}">
                <button type="submit" class="btn btn-primary">Suivant</button>
            </form>
        </div>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Lundi</th>
                    <th>Mardi</th>
                    <th>Mercredi</th>
                    <th>Jeudi</th>
                    <th>Vendredi</th>
                    <th>Samedi</th>
                    <th>Dimanche</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <!-- Cases vides avant le premier jour du mois -->
                    <c:forEach var="i" begin="1" end="${premierJourDuMois - 1}">
                        <td></td>
                    </c:forEach>

                    <!-- Affichage des jours du mois -->
                    <c:forEach var="jour" begin="1" end="${nombreDeJours}">
                        <td class="current-month">
                            <form method="post" action="calendrier">
                                <input type="hidden" name="jour" value="${jour}">
                                <input type="hidden" name="mois" value="${dateCourante}">
                                <button type="submit" class="btn btn-link">${jour}</button>
                            </form>
                            <div>Clicks : ${clickCounters[jour]}</div>
                        </td>
                        <!-- Nouvelle ligne après chaque dimanche -->
                        <c:if test="${(jour + premierJourDuMois - 1) % 7 == 0}">
                            </tr><tr>
                        </c:if>
                    </c:forEach>
                </tr>
            </tbody>
        </table>
    </div>
</body>
</html>
